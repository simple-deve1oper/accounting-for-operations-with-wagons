package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Базовая информация о вагоне")
public class WagonBasicDTO {
    @Schema(description = "Номер вагона")
    private String wagonNumber;
    @Schema(description = "Номенклатура груза")
    private CargoDTO cargoDTO;
    @Schema(description = "Вес груза в вагоне")
    private Double cargoWeight;
    @Schema(description = "Вес вагона")
    private Double wagonWeight;
}
