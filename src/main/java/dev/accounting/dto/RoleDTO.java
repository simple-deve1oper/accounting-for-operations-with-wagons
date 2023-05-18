package dev.accounting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для описания DTO типа Role
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Информация о роли пользователя")
public class RoleDTO {
    @Schema(description = "Логин")
    private String login;   // логин
    @Schema(description = "Роль")
    private String role;    // роль
}
