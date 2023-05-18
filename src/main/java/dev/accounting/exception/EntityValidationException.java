package dev.accounting.exception;

/**
 * Класс исключение для описания ошибок сущностей, которые не прошли валидацию
 * @version 1.0
 */
public class EntityValidationException extends RuntimeException {
    public EntityValidationException() {}

    public EntityValidationException(String message) {
        super(message);
    }
}
