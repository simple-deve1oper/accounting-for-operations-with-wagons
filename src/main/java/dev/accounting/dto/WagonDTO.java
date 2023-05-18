package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс для описания DTO типа Wagon
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о вагоне")
public class WagonDTO {
    @Schema(description = "Идентификатор")
    private Long id;                // идентификатор
    @NotBlank(message = "Поле number не может быть пустым")
    @Schema(description = "Номер")
    private String number;          // номер
    @NotNull(message = "Поле type не может быть пустым")
    @Schema(description = "Тип")
    private TypeDTO type;           // тип
    @NotNull(message = "Поле tareWeight не может быть пустым")
    @Schema(description = "Вес тары")
    private Double tareWeight;      // вес тары
    @NotNull(message = "Поле loadCapacity не может быть пустым")
    @Schema(description = "Грузоподъемность")
    private Double loadCapacity;    // грузоподъемность

    public WagonDTO(String number, TypeDTO type, Double tareWeight, Double loadCapacity) {
        this(null, number, type, tareWeight, loadCapacity);
    }
}
