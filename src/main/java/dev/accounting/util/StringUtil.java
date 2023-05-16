package dev.accounting.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Класс для работы со строками
 * @version 1.0
 */
public class StringUtil {
    /**
     * Получение строки ошибок из объекта типа BindingResult
     * @param bindingResult - объекта типа BindingResult
     * @return строка с ошибками
     */
    public static String getErrorsFromValidation(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        StringBuilder errors = new StringBuilder();
        fieldErrors.stream().forEach(
                fieldError -> errors.append(String.format("%s;", fieldError.getDefaultMessage()))
        );

        return errors.toString();
    }
}
