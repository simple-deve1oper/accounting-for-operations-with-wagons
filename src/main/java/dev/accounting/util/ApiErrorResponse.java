package dev.accounting.util;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Класс для представления ответа об ошибке
 * @param code - код
 * @param message - сообщение
 * @param dateTime - дата и время
 * @version 1.0
 */
@Schema(description = "Информация об ошибке")
public record ApiErrorResponse(@Schema(description = "Код") int code, @Schema(description = "Сообщение") String message, @Schema(description = "Дата и время", type="string", format = "date-time") LocalDateTime dateTime) {
    public ApiErrorResponse(int code, String message) {
        this(code, message, LocalDateTime.now());
    }
}
