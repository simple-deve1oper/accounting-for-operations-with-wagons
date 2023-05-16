package dev.accounting.util;

import dev.accounting.exception.EntityValidationException;
import org.springframework.validation.BindingResult;

/**
 * Класс для работы с данными
 * @version 1.0
 */
public class DataUtil {
    public static void checkValidation(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errors = StringUtil.getErrorsFromValidation(bindingResult);

            throw new EntityValidationException(errors);
        }
    }
}
