package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для описания DTO типа Type
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о типе вагона")
public class TypeDTO {
    @Schema(description = "Идентификатор")
    private Long id;        // идентификатор
    @Schema(description = "Наименование")
    private String name;    // наименование
}
