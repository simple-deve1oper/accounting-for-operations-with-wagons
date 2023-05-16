package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о пути")
public class PathwayDTO {
    @Schema(description = "Идентификатор")
    private Long id;
    @NotNull(message = "Поле station не может быть пустым")
    @Schema(description = "Станция")
    private StationDTO station;
    @NotNull(message = "Поле number не может быть пустым")
    @Min(value = 1, message = "Минимальное значение поле number 1")
    @Schema(description = "Номер")
    private Integer number;
}
