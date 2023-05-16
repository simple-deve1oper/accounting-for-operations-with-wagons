package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о документе для приема вагона")
public class DocumentDTO {
    @Schema(description = "Идентификатор")
    private Long id;
    @Schema(description = "Порядковый номер")
    private Integer serialNumber;
    @Schema(description = "Номер вагона")
    private String wagonNumber;
    @Schema(description = "Номенклатура груза")
    private CargoDTO cargoDTO;
    @Schema(description = "Вес груза в вагоне")
    private Double cargoWeight;
    @Schema(description = "Вес вагона")
    private Double wagonWeight;
    @Schema(description = "Путь")
    private PathwayDTO pathway;
    @Schema(description = "Дата отбытия")
    private LocalDateTime departureDate;
}
