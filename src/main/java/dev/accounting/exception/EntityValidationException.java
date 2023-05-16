package dev.accounting.exception;

public class EntityValidationException extends RuntimeException {
    public EntityValidationException() {}

    public EntityValidationException(String message) {
        super(message);
    }
}
