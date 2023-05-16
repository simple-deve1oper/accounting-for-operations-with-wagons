package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Модель станции")
public class StationModelDTO {
    @Schema(description = "Идентификатор")
    private Long id;
    @NotBlank(message = "Поле name не может быть пустым")
    @Schema(description = "Наименование")
    private String name;
    @NotNull(message = "Поле pathways не может быть пустым")
    @Schema(description = "Пути")
    private List<PathwayModelDTO> pathways;
}
