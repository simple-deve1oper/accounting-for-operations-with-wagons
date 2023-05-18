package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс DTO о вагоне для документов
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Базовая информация о вагоне для документов")
public class WagonBasicDTO {
    @Schema(description = "Номер вагона")
    private String wagonNumber; // номер вагона
    @Schema(description = "Номенклатура груза")
    private CargoDTO cargoDTO;  // груз
    @Schema(description = "Вес груза в вагоне")
    private Double cargoWeight; // вес груза
    @Schema(description = "Вес вагона")
    private Double wagonWeight; // вес вагона
}
