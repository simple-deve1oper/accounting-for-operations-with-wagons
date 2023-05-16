package dev.accounting.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class StringUtilTest {
    @Mock
    private BindingResult bindingResult;

    @Test
    void stringUtil_getErrorsFromValidation() {
        Mockito.when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("object", "field", "default message")));
        String message = StringUtil.getErrorsFromValidation(bindingResult);
        Assertions.assertNotNull(message);
        Assertions.assertFalse(message.isBlank());
        Assertions.assertEquals(message, "default message;");

        Mockito.verify(bindingResult, Mockito.times(1)).getFieldErrors();
    }
}
