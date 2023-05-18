package dev.accounting.util;

import dev.accounting.exception.EntityValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

/**
 * Тестирование класса DataUtil
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class DataUtilTest {
    @Mock
    private BindingResult bindingResult;

    @Test
    void dataUtil_checkValidation() {
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getFieldErrors())
                .thenReturn(Collections.singletonList(new FieldError("object", "field", "default message")));
        EntityValidationException exception = Assertions.assertThrows(
                EntityValidationException.class,
                () -> DataUtil.checkValidation(bindingResult)
        );
        String expectedMessage = "default message;";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(bindingResult, Mockito.times(1)).hasErrors();
        Mockito.verify(bindingResult, Mockito.times(1)).getFieldErrors();
    }
}
