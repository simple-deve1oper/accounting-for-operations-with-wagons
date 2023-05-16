package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о приёме вагонов")
public class ReceptionDTO {
    @Schema(description = "Список вагонов")
    private List<WagonBasicDTO> wagons;
    @Schema(description = "Путь")
    private PathwayDTO pathway;
}
