package dev.accounting.controller;

import dev.accounting.dto.CargoDTO;
import dev.accounting.entity.Cargo;
import dev.accounting.exception.EntityAlreadyExistsException;
import dev.accounting.exception.InputErrorException;
import dev.accounting.service.CargoService;
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

/**
 * Контроллер для работы со справочником номенклатур грузов
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/cargos")
@Tag(name = "Методы для работы со справочником номенклатур грузов")
@SecurityRequirement(name = "basicAuth")
public class CargoNomenclatureController {
    @Autowired
    private CargoService cargoService;

    @GetMapping("/all")
    @Operation(summary = "Получение всех грузов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = CargoDTO.class)))
            })
    })
    public ResponseEntity<List<CargoDTO>> getAllCargos() {
        List<Cargo> cargos = cargoService.findAll();
        if (cargos.isEmpty()) {
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<CargoDTO> cargosDTO = cargos.stream()
                .map(c -> DataTransformation.convertingCargoDataFromEntityToDTO(c))
                .collect(Collectors.toList());

        return ResponseEntity.ok(cargosDTO);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(schema = @Schema(implementation = CargoDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Cargo not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<CargoDTO> getCargoById(
            @Parameter(description = "Идентификатор") @PathVariable("id") Long id
    ) {
        Cargo cargo = cargoService.findById(id);
        CargoDTO cargoDTO = DataTransformation.convertingCargoDataFromEntityToDTO(cargo);

        return ResponseEntity.ok(cargoDTO);
    }

    @PostMapping("/create")
    @Operation(summary = "Создание нового груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cargo created", content = {
                    @Content(schema = @Schema(implementation = CargoDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Validation Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "409", description = "Cargo already exists", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
    })
    public ResponseEntity<CargoDTO> createCargo(
            @Parameter(description = "Объект типа CCargoDTO") @RequestBody @Valid CargoDTO cargoDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);

        if (cargoService.existsByCode(cargoDTO.getCode())) {
            throw new EntityAlreadyExistsException(String.format("Груз с кодом %s уже существует", cargoDTO.getCode()));
        }

        Cargo cargo = DataTransformation.convertingCargoDataFromDTOToEntity(cargoDTO);
        cargo = cargoService.save(cargo);
        cargoDTO = DataTransformation.convertingCargoDataFromEntityToDTO(cargo);

        return new ResponseEntity<>(cargoDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/edit")
    @Operation(summary = "Обновление наименование груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(schema = @Schema(implementation = CargoDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Input Error", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "404", description = "Cargo not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public ResponseEntity<CargoDTO> updateName(
            @Parameter(description = "Идентификатор станции") @PathVariable("id") Long id,
            @Parameter(description = "Новое наименование груза") @RequestParam("name") String name
    ) {
        if (name.isBlank()) {
            throw new InputErrorException("Наименование станции не может быть пустым");
        }

        Cargo cargo = cargoService.findById(id);
        cargo.setName(name);
        cargoService.save(cargo);
        CargoDTO cargoDTO = DataTransformation.convertingCargoDataFromEntityToDTO(cargo);

        return ResponseEntity.ok(cargoDTO);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Удаление существующего груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Cargo not found", content = {
                    @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    public void deleteCargo(
            @Parameter(description = "Идентификатор станции") @PathVariable("id") Long id
    ) {
        Cargo cargo = cargoService.findById(id);
        cargoService.delete(cargo);
    }
}
