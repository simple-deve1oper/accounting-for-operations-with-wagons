package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для описания DTO типа Pathway станционной модели
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Модель пути")
public class PathwayModelDTO {
    @Schema(description = "Идентификатор")
    private Long id;        // идентификатор
    @Schema(description = "Номер")
    private Integer number; // номер
}
