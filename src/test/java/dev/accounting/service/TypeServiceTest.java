package dev.accounting.service;

import dev.accounting.entity.Type;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.TypeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

/**
 * Тестирование класса TypeService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class TypeServiceTest {
    @Mock
    private TypeRepository typeRepository;
    @InjectMocks
    private TypeService typeService;

    private Type type;

    @BeforeEach
    void setUp() {
        type = new Type(1L, "Тип 1", Collections.emptyList());
    }

    @Test
    void typeService_findById() {
        Mockito.when(typeRepository.findById(1L)).thenReturn(Optional.of(type));
        Type typeFromDB = typeService.findById(1L);
        Assertions.assertNotNull(typeFromDB);
        Assertions.assertEquals(1L, typeFromDB.getId());
        Assertions.assertEquals("Тип 1", typeFromDB.getName());
        Assertions.assertTrue(typeFromDB.getWagons().isEmpty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> typeService.findById(56L)
        );

        String expectedMessage = "Тип вагона с идентификатором 56 не найден";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(typeRepository, Mockito.times(2))
                .findById(Mockito.anyLong());
    }

    @Test
    void typeService_existsById() {
        Mockito.when(typeRepository.existsById(1L)).thenReturn(true);
        boolean result = typeService.existsById(1L);
        Assertions.assertTrue(result);

        Mockito.when(typeRepository.existsById(56L)).thenReturn(false);
        result = typeService.existsById(56L);
        Assertions.assertFalse(result);

        Mockito.verify(typeRepository, Mockito.times(2))
                .existsById(Mockito.anyLong());
    }
}
