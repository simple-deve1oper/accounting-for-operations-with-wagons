package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Класс для описания DTO типа Cargo
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о грузе")
public class CargoDTO {
    @Schema(description = "Идентификатор")
    private Long id;        // идентификатор
    @NotNull(message = "Поле code не может быть пустым")
    @Min(value = 1, message = "Минимальное значение поля code 1")
    @Schema(description = "Код")
    private String code;    // код
    @NotBlank(message = "Поле name не может быть пустым")
    @Schema(description = "Наименование")
    private String name;    // наименование
}
