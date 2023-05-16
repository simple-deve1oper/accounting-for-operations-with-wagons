package dev.accounting.controller;

import dev.accounting.dto.WagonDTO;
import dev.accounting.entity.Wagon;
import dev.accounting.exception.EntityAlreadyExistsException;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.exception.EntityValidationException;
import dev.accounting.exception.InputErrorException;
import dev.accounting.service.TypeService;
import dev.accounting.service.WagonService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/passport")
@Tag(name = "Методы для работы со справочником паспорта вагонов")
@SecurityRequirement(name = "basicAuth")
public class WagonPassportController {
    @Autowired
    private WagonService wagonService;
    @Autowired
    private TypeService typeService;

    @GetMapping("/all")
    @Operation(summary = "Получение всех вагонов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = WagonDTO.class)))
            })
    })
    public ResponseEntity<List<WagonDTO>> getAllWagons() {
        List<Wagon> wagons = wagonService.findAll();
        if (wagons.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<WagonDTO> wagonsDTO = DataTransformation.convertingListDataWagonsFromEntityToDTO(wagons);
        return ResponseEntity.ok(wagonsDTO);
    }

    @GetMapping("/{number}")
    @Operation(summary = "Получение вагона по его номеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(schema = @Schema(implementation = WagonDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Input error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Wagon not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<WagonDTO> getWagonByNumber(
            @Parameter(description = "Номер вагона") @PathVariable(name = "number") String number
    ) {
        checkingNumberFromRequest(number);
        Wagon wagon = wagonService.findByNumber(number);
        WagonDTO wagonDTO = DataTransformation.convertingWagonDataFromEntityToDTO(wagon);
        return ResponseEntity.ok(wagonDTO);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание информации о новом вагоне")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wagon created", content = {
                    @Content(schema = @Schema(implementation = WagonDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "Wagon already Exists", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<WagonDTO> createWagon(
            @Parameter(description = "Объект типа WagonDTO") @RequestBody @Valid WagonDTO wagonDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);
        String number = wagonDTO.getNumber();
        if (wagonService.existByNumber(number)) {
            throw new EntityAlreadyExistsException(String.format("Вагон с номером '%s' уже существует", number));
        }
        wagonTypeCheck(wagonDTO);

        Wagon wagon = DataTransformation.convertingWagonDataFromDTOToEntity(wagonDTO, typeService);
        wagon = wagonService.save(wagon);
        WagonDTO wagonDTOFromDB = DataTransformation.convertingWagonDataFromEntityToDTO(wagon);

        return new ResponseEntity<>(wagonDTOFromDB, HttpStatus.CREATED);
    }

    @PutMapping("/{number}/update")
    @Operation(summary = "Обновление информации о существующем вагоне")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wagon created", content = {
                    @Content(schema = @Schema(implementation = WagonDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Validation or input error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Wagon not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<WagonDTO> updateWagon(
            @Parameter(description = "Номер вагона") @PathVariable(name = "number") String number,
            @Parameter(description = "Объект типа WagonDTO") @RequestBody @Valid WagonDTO wagonDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        checkingNumberFromRequest(number);
        DataUtil.checkValidation(bindingResult);

        checkingNumberWagon(number);
        wagonTypeCheck(wagonDTO);

        Long id = wagonService.getIdByNumber(number);
        wagonDTO.setNumber(number);
        Wagon wagon = DataTransformation.convertingWagonDataFromDTOToEntity(wagonDTO, typeService);
        wagon.setId(id);
        wagon = wagonService.save(wagon);
        WagonDTO wagonDTOFromDB = DataTransformation.convertingWagonDataFromEntityToDTO(wagon);
        return ResponseEntity.ok(wagonDTOFromDB);
    }

    @DeleteMapping("/{number}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удаление вагона")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wagon created"),
            @ApiResponse(responseCode = "400", description = "Input error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Wagon not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public void deleteWagon(
            @Parameter(description = "Номер вагона") @PathVariable(name = "number") String number
    ) {
        checkingNumberFromRequest(number);
        checkingNumberWagon(number);

        Wagon wagon = wagonService.findByNumber(number);
        wagonService.delete(wagon);
    }

    private void wagonTypeCheck(WagonDTO wagonDTO) {
        if (wagonDTO.getType().getId() == null || wagonDTO.getType().getName() == null) {
            throw new EntityValidationException(String.format("Ошибка валидации типа вагона"));
        }
    }

    private void checkingNumberWagon(String number) {
        if (!wagonService.existByNumber(number)) {
            throw new EntityNotFoundException(String.format("Вагон с номером '%s' не существует", number));
        }
    }

    private void checkingNumberFromRequest(String number) {
        if (number.length() > 7) {
            throw new InputErrorException("Номер вагона не может содержать более 7 цифр");
        }
    }
}
