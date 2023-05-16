package dev.accounting.exception.handler;

import dev.accounting.exception.EntityAlreadyExistsException;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.exception.EntityValidationException;
import dev.accounting.exception.InputErrorException;
import dev.accounting.util.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Класс для описания обработчиков исключений
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class ExceptionApiHandler {
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> entityNotFound(EntityNotFoundException exception) {
        var code = HttpStatus.NOT_FOUND.value();
        var message = exception.getMessage();
        ApiErrorResponse response = createResponse(code, message);
        log.error("ExceptionApiHandler.entityNotFound(EntityNotFoundException exception) -> response: " + response);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> validation(EntityValidationException exception) {
        var code = HttpStatus.BAD_REQUEST.value();
        var message = exception.getMessage();
        ApiErrorResponse response = createResponse(code, message);
        log.error("ExceptionApiHandler.validation(EntityValidationException exception) -> response: " + response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> inputException(InputErrorException exception) {
        var code = HttpStatus.BAD_REQUEST.value();
        var message = exception.getMessage();
        ApiErrorResponse response = createResponse(code, message);
        log.error("ExceptionApiHandler.inputException(InputErrorException exception) -> response: " + response);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> entityExists(EntityAlreadyExistsException exception) {
        var code = HttpStatus.CONFLICT.value();
        var message = exception.getMessage();
        ApiErrorResponse response = createResponse(code, message);
        log.error("ExceptionApiHandler.entityExists(EntityAlreadyExistsException exception) -> response: " + response);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    /**
     * Создание объекта типа ApiErrorResponse
     * @param code - код
     * @param message - сообщение
     * @return объект типа ApiErrorResponse
     */
    private ApiErrorResponse createResponse(int code, String message) {
        return new ApiErrorResponse(code, message);
    }
}
