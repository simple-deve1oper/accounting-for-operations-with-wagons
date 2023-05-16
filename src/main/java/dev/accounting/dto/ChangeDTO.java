package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация для изменения порядка вагонов")
public class ChangeDTO {
    @NotNull(message = "Поле wagonsNumber не может быть пустым")
    @Schema(description = "Список вагонов")
    private List<String> wagonNumbers;
    @NotNull(message = "Поле pathway не может быть пустым")
    @Schema(description = "Путь")
    private PathwayDTO pathway;
    @NotNull(message = "Поле first не может быть пустым")
    @Schema(description = "Расположение в начале состава")
    private Boolean first;
}
