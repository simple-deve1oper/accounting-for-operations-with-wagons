package dev.accounting.controller;

import dev.accounting.dto.PathwayDTO;
import dev.accounting.dto.StationModelDTO;
import dev.accounting.entity.Pathway;
import dev.accounting.entity.Station;
import dev.accounting.exception.EntityValidationException;
import dev.accounting.exception.InputErrorException;
import dev.accounting.service.PathwayService;
import dev.accounting.service.StationService;
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/model")
@Tag(name = "Методы для работы со справочником станционной модели")
@SecurityRequirement(name = "basicAuth")
public class StationModelController {
    @Autowired
    private StationService stationService;
    @Autowired
    private PathwayService pathwayService;

    @GetMapping("/stations/all")
    @Operation(summary = "Получение всех станций и их путей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = StationModelDTO.class)))
            })
    })
    public ResponseEntity<List<StationModelDTO>> getAllStations() {
        List<Station> stations = stationService.findAll();
        if (stations.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<StationModelDTO> stationsDTO = stations.stream()
                .map(station -> DataTransformation.convertingStationFromEntityToModelDTO(station))
                .collect(Collectors.toList());

        return ResponseEntity.ok(stationsDTO);
    }

    @GetMapping("/stations/{id}")
    @Operation(summary = "Получение станции и её путей по идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(schema = @Schema(implementation = StationModelDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Station not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<StationModelDTO> getStationById(
            @Parameter(description = "Идентификатор станции") @PathVariable("id") Long id
    ) {
        Station station = stationService.findById(id);
        StationModelDTO stationDTO = DataTransformation.convertingStationFromEntityToModelDTO(station);

        return ResponseEntity.ok(stationDTO);
    }

    @PostMapping("/stations/create")
    @Operation(summary = "Создание новой станции и путей для неё")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Station created", content = {
                    @Content(schema = @Schema(implementation = StationModelDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<StationModelDTO> createStation(
            @Parameter(description = "Объект типа StationModelDTO") @RequestBody @Valid StationModelDTO stationModelDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);

        if (stationModelDTO.getPathways().isEmpty()) {
            throw new EntityValidationException("Список путей не может быть пустым");
        }

        Station station = DataTransformation.convertingStationFromModelDTOToEntity(stationModelDTO);
        List<Pathway> pathways = station.getPathways();
        station.setPathways(null);
        station = stationService.save(station);
        for(Pathway pathway : pathways) {
            pathway.setStation(station);
        }
        station.setPathways(pathways);
        pathwayService.saveAll(pathways);
        stationModelDTO = DataTransformation.convertingStationFromEntityToModelDTO(station);

        return new ResponseEntity<>(stationModelDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/stations/{id}/edit")
    @Operation(summary = "Обновление наименования станции")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(schema = @Schema(implementation = StationModelDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Input Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Station not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<StationModelDTO> updateNameStation(
            @Parameter(description = "Идентификатор станции") @PathVariable("id") Long id,
            @Parameter(description = "Новое наименование станции") @RequestParam("name") String name
    ) {
        if (name.isBlank()) {
            throw new InputErrorException("Наименование станции не может быть пустым");
        }

        Station station = stationService.findById(id);
        station.setName(name);
        station = stationService.save(station);
        StationModelDTO stationModelDTO = DataTransformation.convertingStationFromEntityToModelDTO(station);

        return ResponseEntity.ok(stationModelDTO);
    }

    @DeleteMapping("/stations/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удаление существующей станции")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Station not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public void deleteStation(
            @Parameter(description = "Идентификатор станции") @PathVariable("id") Long id
    ) {
        Station station = stationService.findById(id);
        stationService.delete(station);
    }

    @PostMapping("/pathways/create")
    @Operation(summary = "Создание нового пути существующей станции")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pathway created", content = {
                    @Content(schema = @Schema(implementation = PathwayDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<PathwayDTO> createPathway(
            @Parameter(description = "Объект типа PathwayDTO") @RequestBody @Valid PathwayDTO pathwayDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);

        Pathway pathway = DataTransformation.convertingPathwayDataFromDTOToEntity(pathwayDTO, stationService);
        pathway = pathwayService.save(pathway);
        pathwayDTO = DataTransformation.convertingPathwayDataFromEntityToDTO(pathway);

        return new ResponseEntity<>(pathwayDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/pathways/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удаление существующего пути")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Pathway not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public void deletePathway(
            @Parameter(description = "Идентификатор пути") @PathVariable("id") Long id
    ) {
        Pathway pathway = pathwayService.findById(id);
        pathwayService.delete(pathway);
    }
}
