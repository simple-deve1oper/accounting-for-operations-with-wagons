package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о роли пользователя")
public class RoleDTO {
    @Schema(description = "Логин")
    private String login;
    @Schema(description = "Роль")
    private String role;
}
