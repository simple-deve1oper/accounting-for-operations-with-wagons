package dev.accounting.exception;

/**
 * Класс исключение для описания ошибок сущностей, которые уже существуют
 * @version 1.0
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException() {}

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
