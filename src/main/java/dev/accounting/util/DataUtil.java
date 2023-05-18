package dev.accounting.util;

import dev.accounting.exception.EntityValidationException;
import org.springframework.validation.BindingResult;

/**
 * Класс для работы с данными
 * @version 1.0
 */
public class DataUtil {
    /**
     * Проверка объекта типа BindingResult на ошибки
     * @param bindingResult - объект типа BindingResult
     */
    public static void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = StringUtil.getErrorsFromValidation(bindingResult);

            throw new EntityValidationException(errors);
        }
    }
}
