package dev.accounting.exception;

public class InputErrorException extends RuntimeException {
    public InputErrorException() {}

    public InputErrorException(String message) {
        super(message);
    }
}
