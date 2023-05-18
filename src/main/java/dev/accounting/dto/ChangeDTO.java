package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Класс DTO для перестановки вагонов внутри станции
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация для перестановки вагонов внутри станции")
public class ChangeDTO {
    @NotNull(message = "Поле wagonsNumber не может быть пустым")
    @Schema(description = "Список вагонов")
    private List<String> wagonNumbers;  // список вагонов
    @NotNull(message = "Поле pathway не может быть пустым")
    @Schema(description = "Путь")
    private PathwayDTO pathway;         // путь
    @NotNull(message = "Поле first не может быть пустым")
    @Schema(description = "Расположение в начале состава")
    private Boolean first;              // Расположение в начале состава
}
