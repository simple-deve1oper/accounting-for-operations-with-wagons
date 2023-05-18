package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для описания DTO типа Station
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о станции")
public class StationDTO {
    @Schema(description = "Идентификатор")
    private Long id;        // идентификатор
    @Schema(description = "Наименование")
    private String name;    // наименование
}
