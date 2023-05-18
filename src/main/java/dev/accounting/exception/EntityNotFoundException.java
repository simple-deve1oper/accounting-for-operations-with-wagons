package dev.accounting.exception;

/**
 * Класс-исключение для описания ошибок не найденных сущностей
 * @version 1.0
 */
public class EntityNotFoundException extends RuntimeException{
    public EntityNotFoundException() {}

    public EntityNotFoundException(String message) {
        super(message);
    }
}
