package dev.accounting.exception;

/**
 * Класс-исключение для описания ошибок ввода
 * @version 1.0
 */
public class InputErrorException extends RuntimeException {
    public InputErrorException() {}

    public InputErrorException(String message) {
        super(message);
    }
}
