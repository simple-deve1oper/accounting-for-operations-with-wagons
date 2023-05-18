package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Класс для описания DTO типа Document
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о документе для приема вагона")
public class DocumentDTO {
    @Schema(description = "Идентификатор")
    private Long id;                        // идентификатор
    @Schema(description = "Порядковый номер")
    private Integer serialNumber;           // порядковый номер
    @Schema(description = "Номер вагона")
    private String wagonNumber;             // номер вагона
    @Schema(description = "Номенклатура груза")
    private CargoDTO cargoDTO;              // груз
    @Schema(description = "Вес груза в вагоне")
    private Double cargoWeight;             // вес груза
    @Schema(description = "Вес вагона")
    private Double wagonWeight;             // вес вагона
    @Schema(description = "Путь")
    private PathwayDTO pathway;             // путь
    @Schema(description = "Дата убытия")
    private LocalDateTime departureDate;    // дата убытия
}
