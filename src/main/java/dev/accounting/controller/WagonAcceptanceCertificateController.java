package dev.accounting.controller;

import dev.accounting.dto.*;
import dev.accounting.entity.Document;
import dev.accounting.entity.Pathway;
import dev.accounting.exception.EntityAlreadyExistsException;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.exception.EntityValidationException;
import dev.accounting.exception.InputErrorException;
import dev.accounting.service.*;
import dev.accounting.util.ApiErrorResponse;
import dev.accounting.util.DataTransformation;
import dev.accounting.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Методы для работы со справочником натурного листа для приема вагонов")
@SecurityRequirement(name = "basicAuth")
public class WagonAcceptanceCertificateController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private WagonService wagonService;
    @Autowired
    private CargoService cargoService;
    @Autowired
    private PathwayService pathwayService;

    @GetMapping("/all")
    @Operation(summary = "Получение всех листов для приема вагонов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentDTO.class)))
            })
    })
    public ResponseEntity<List<DocumentDTO>> getAllDocuments() {
        List<Document> documents = documentService.findAll();
        if (documents.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<DocumentDTO> documentsDTO = documents.stream()
                .map(d -> DataTransformation.convertingDocumentDataFromEntityToDTO(d))
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentsDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение листа для приёма вагонов по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(schema = @Schema(implementation = DocumentDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Document not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<DocumentDTO> getDocumentById(
            @Parameter(description = "Идентификатор документа") @PathVariable("id") Long id
    ) {
        Document document = documentService.findById(id);
        DocumentDTO documentDTO = DataTransformation.convertingDocumentDataFromEntityToDTO(document);

        return ResponseEntity.ok(documentDTO);
    }

    @GetMapping("/{pathwayId}/pathway")
    @Operation(summary = "Получение листов для приема вагонов по заданному пути, вагоны которых находятся на предприятии")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = DocumentDTO.class)))
            })
    })
    public ResponseEntity<List<DocumentDTO>> getAllDocumentsById(
            @Parameter(description = "Идентификатор пути") @PathVariable("pathwayId") Long pathwayId
    ) {
        List<Document> documents = documentService.findByDepartureDateIsNullAndPathway_Id(pathwayId, Sort.by("serialNumber"));
        if (documents.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<DocumentDTO> documentsDTO = documents.stream()
                .map(d -> DataTransformation.convertingDocumentDataFromEntityToDTO(d))
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentsDTO);
    }

    @PostMapping("/create")
    @Operation(summary = "Прием вагонов на предприятие")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Document created", content = {
                    @Content(schema = @Schema(implementation = DocumentDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Wagon not found", content = {
                @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "Document already exists", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<List<DocumentDTO>> createDocuments(
            @Parameter(description = "Объект типа ReceptionDTO") @RequestBody @Valid ReceptionDTO receptionDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);

        List<String> wagonNumbers = receptionDTO.getWagons().stream()
                .map(w -> w.getWagonNumber())
                .collect(Collectors.toList());
        checkExistsWagonsInDB(wagonNumbers);
        checkExistsWagonsAtEnterprise(wagonNumbers);

        List<Document> documents = DataTransformation.createListDocumentsFromReceptionDTO(receptionDTO, cargoService, pathwayService);
        Integer maxSerialNumber = documentService.maxSerialNumberByDepartureDateIsNullAndPathway_Id(receptionDTO.getPathway().getId());
        int count = 1;
        if (maxSerialNumber != null) {
            count = maxSerialNumber + 1;
        }
        settingSerialNumber(documents, count);
        documentService.saveAll(documents);
        List<DocumentDTO> documentsDTO = documents.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());

        return new ResponseEntity<>(documentsDTO, HttpStatus.CREATED);
    }

    @PutMapping("/reorder")
    @Operation(summary = "Перестановка вагонов внутри станции")
    public ResponseEntity<List<DocumentDTO>> reorder(
            @Parameter(description = "Объект типа ChangeDTO") @RequestBody @Valid ChangeDTO changeDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);
        List<String> wagonNumbers = changeDTO.getWagonNumbers();
        checkExistsWagonsInDB(wagonNumbers);
        List<Document> documents = changeDTO.getWagonNumbers().stream()
                .map(wagonNumber -> documentService.findByWagonNumberAndDepartureDateIsNull(wagonNumber))
                .collect(Collectors.toList());
        PathwayDTO pathwayDTOOldValue = DataTransformation.convertingPathwayDataFromEntityToDTO(documents.get(0).getPathway());
        stationsAndPathwaysComparison(documents, changeDTO.getPathway());
        int size = documents.size();
        Pathway pathway = pathwayService.findById(changeDTO.getPathway().getId());
        if (changeDTO.getFirst()) {
            List<Document> documentsFromDB = documentService.findByDepartureDateIsNullAndPathway_Id(pathway.getId(), Sort.by("serialNumber"));
            if (!documentsFromDB.isEmpty()) {
                int count = size + 1;
                settingSerialNumber(documentsFromDB, count);
                documentService.saveAll(documentsFromDB);
            }
            settingSerialNumber(documents, 1);
        } else {
            Integer maxSerialNumber = documentService.maxSerialNumberByDepartureDateIsNullAndPathway_Id(pathway.getId());
            int count = 1;
            if (maxSerialNumber != null) {
                count = maxSerialNumber + 1;
            }
            settingSerialNumber(documents, count);
        }
        documents.forEach(document -> document.setPathway(pathway));
        documents = documentService.saveAll(documents);
        documents.forEach(document -> pathway.getDocuments().add(document));

        List<Document> documentsFromDBWithOldPathway = documentService.findByDepartureDateIsNullAndPathway_Id(pathwayDTOOldValue.getId(), Sort.by("serialNumber"));
        settingSerialNumber(documentsFromDBWithOldPathway, 1);
        documentService.saveAll(documentsFromDBWithOldPathway);

        List<Document> documentsFromDB = documentService
                .findByDepartureDateIsNullAndPathway_Id(pathway.getId(), Sort.by("serialNumber"));
        List<DocumentDTO> documentsDTOFromDB = documentsFromDB.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentsDTOFromDB);
    }

    @PatchMapping("/departure/{pathwayId}/pathway")
    @Operation(summary = "Убытие вагонов на сеть РЖД")
    public ResponseEntity<List<DocumentDTO>> departure(
            @Parameter(description = "Идентификатор пути") @PathVariable("pathwayId") Long pathwayId,
            @Parameter(description = "Количество вагонов для убытия") @RequestParam("quantity") Integer quantity
    ) {
        Pathway pathway = pathwayService.findById(pathwayId);
        Integer wagonQuantities = documentService.maxSerialNumberByDepartureDateIsNullAndPathway_Id(pathway.getId());
        if (wagonQuantities == null || wagonQuantities.intValue() == 0) {
            throw new EntityNotFoundException("Вагонов для убытия не найдено");
        }
        if (quantity.intValue() > wagonQuantities.intValue()) {
            throw new InputErrorException("Заданное количество для убытия вагонов больше самих вагонов");
        }

        List<Document> documents = documentService.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(pathway.getId(), quantity);
        LocalDateTime departureDate = LocalDateTime.now();
        documents.forEach(document -> document.setDepartureDate(departureDate));
        documents = documentService.saveAll(documents);

        List<Document> documentsFromDB = documentService
                .findByDepartureDateIsNullAndPathway_Id(pathway.getId(), Sort.by("serialNumber"));
        settingSerialNumber(documentsFromDB, 1);
        documentService.saveAll(documentsFromDB);

        List<DocumentDTO> documentsDTO = documents.stream()
                .map(document -> DataTransformation.convertingDocumentDataFromEntityToDTO(document))
                .collect(Collectors.toList());

        return ResponseEntity.ok(documentsDTO);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удаление документа")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Document not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public void deleteDocument(
            @Parameter(description = "Идентификатор документа") @PathVariable("id") Long id
    ) {
        Document document = documentService.findById(id);
        if (document.getDepartureDate() == null) {
            throw new EntityValidationException("Документ нельзя удалить, т.к. вагон ещё находится на предприятии");
        }
        documentService.delete(document);
    }

    /**
     * Проверка на существование вагонов на предприятии
     * @param wagonNumbers - список с номерами вагонов
     */
    private void checkExistsWagonsAtEnterprise(List<String> wagonNumbers) {
        for (String wagonNumber : wagonNumbers) {
            if (documentService.existsByWagonNumberAndDepartureDateIsNull(wagonNumber)) {
                throw new EntityAlreadyExistsException(String.format("Вагон с номером '%s' уже стоит на станции", wagonNumber));
            }
        }
    }

    /**
     * Проверка на существование вагонов в БД
     * @param wagonNumbers - список с номерами вагонов
     */
    private void checkExistsWagonsInDB(List<String> wagonNumbers) {
        for (String wagonNumber : wagonNumbers) {
            if (!wagonService.existByNumber(wagonNumber)) {
                throw new EntityNotFoundException(String.format("Вагона с номером '%d' не существует", wagonNumber));
            }
        }
    }

    /**
     * Установка порядкового номера вагонам
     * @param documents - список документов
     * @param count - счётчик порядкового номера
     */
    private void settingSerialNumber(List<Document> documents, int count) {
        for (Document document : documents) {
            document.setSerialNumber(count);
            count++;
        }
    }

    /**
     * Сравнение станции и пути
     * @param documents - список документов
     * @param pathwayDTO - объект типа PathwayDTO
     */
    private void stationsAndPathwaysComparison(List<Document> documents, PathwayDTO pathwayDTO) {
        for (Document document : documents) {
            PathwayDTO pathwayDTOFromObject = DataTransformation.convertingPathwayDataFromEntityToDTO(document.getPathway());
            if (pathwayDTOFromObject.equals(pathwayDTO)) {
                throw new EntityValidationException("Нельзя перемещать вагоны на тот же путь, где они находятся");
            }
            StationDTO stationDTOFromObject = pathwayDTOFromObject.getStation();
            if (!stationDTOFromObject.equals(pathwayDTO.getStation())) {
                throw new EntityValidationException("Станции разные! Перемещать вагоны на другие пути можно только в пределах одной станции!");
            }
        }
    }
}
