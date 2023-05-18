package dev.accounting.service;

import dev.accounting.entity.Type;
import dev.accounting.entity.Wagon;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.WagonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Тестирование класса WagonService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class WagonServiceTest {
    @Mock
    private WagonRepository wagonRepository;
    @InjectMocks
    private WagonService wagonService;

    private Wagon wagon1;
    private Wagon wagon2;
    private Type type;

    @BeforeEach
    void setUp() {
        type = new Type(1L, "Тип 1", Collections.emptyList());
        wagon1 = new Wagon(123L, "1122334", type, 1.1, 1.3);
        wagon2 = new Wagon(155L, "4433221", type, 2.2, 2.6);
        type.setWagons(Arrays.asList(wagon1, wagon2));
    }

    @Test
    void wagonService_findAll() {
        Mockito.when(wagonRepository.findAll()).thenReturn(Arrays.asList(wagon1, wagon2));
        List<Wagon> wagonsFromDB = wagonService.findAll();
        Assertions.assertNotNull(wagonsFromDB);
        Assertions.assertFalse(wagonsFromDB.isEmpty());
        Assertions.assertEquals(2, wagonsFromDB.size());

        Mockito.verify(wagonRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void wagonService_findByNumber() {
        Mockito.when(wagonRepository.findByNumber("4433221")).thenReturn(Optional.of(wagon2));
        Wagon wagonFromDB = wagonService.findByNumber("4433221");
        Assertions.assertNotNull(wagonFromDB);
        Assertions.assertEquals(155L, wagonFromDB.getId());
        Assertions.assertEquals("4433221", wagonFromDB.getNumber());
        Assertions.assertNotNull(wagonFromDB.getType());
        Assertions.assertEquals(1L, wagonFromDB.getType().getId());
        Assertions.assertEquals("Тип 1", wagonFromDB.getType().getName());
        Assertions.assertEquals(2.2, wagonFromDB.getTareWeight());
        Assertions.assertEquals(2.6, wagonFromDB.getLoadCapacity());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> wagonService.findByNumber("5555999")
        );

        String expectedMessage = "Вагон с номером '5555999' не найден";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(wagonRepository, Mockito.times(2))
                .findByNumber(Mockito.anyString());
    }

    @Test
    void wagonService_existByNumber() {
        Mockito.when(wagonRepository.existsByNumber("4433221")).thenReturn(true);
        boolean result = wagonService.existByNumber("4433221");
        Assertions.assertTrue(result);

        Mockito.when(wagonRepository.existsByNumber("1111222")).thenReturn(false);
        result = wagonService.existByNumber("1111222");
        Assertions.assertFalse(result);

        Mockito.verify(wagonRepository, Mockito.times(2))
                .existsByNumber(Mockito.anyString());
    }

    @Test
    void wagonService_getIdByNumber() {
        Mockito.when(wagonRepository.getIdByNumber("1122334")).thenReturn(123L);
        Long id = wagonService.getIdByNumber("1122334");
        Assertions.assertNotNull(id);
        Assertions.assertEquals(123, id);

        Mockito.when(wagonRepository.getIdByNumber("9999999")).thenReturn(null);
        id = wagonService.getIdByNumber("9999999");
        Assertions.assertNull(id);

        Mockito.verify(wagonRepository, Mockito.times(2))
                .getIdByNumber(Mockito.anyString());
    }

    @Test
    void wagonService_save() {
        Wagon wagonFromRequest = new Wagon();
        wagonFromRequest.setNumber("1122334");
        wagonFromRequest.setType(type);
        wagonFromRequest.setTareWeight(1.1);
        wagonFromRequest.setLoadCapacity(1.3);
        Mockito.when(wagonRepository.save(Mockito.any(Wagon.class))).thenReturn(wagon1);
        Wagon wagonFromDB = wagonService.save(wagonFromRequest);
        Assertions.assertNotNull(wagonFromDB);
        Assertions.assertEquals(123L, wagonFromDB.getId());
        Assertions.assertEquals("1122334", wagonFromDB.getNumber());
        Assertions.assertNotNull(wagonFromDB.getType());
        Assertions.assertEquals(1L, wagonFromDB.getType().getId());
        Assertions.assertEquals("Тип 1", wagonFromDB.getType().getName());
        Assertions.assertEquals(1.1, wagonFromDB.getTareWeight());
        Assertions.assertEquals(1.3, wagonFromDB.getLoadCapacity());

        Mockito.verify(wagonRepository, Mockito.times(1))
                .save(Mockito.any(Wagon.class));
    }

    @Test
    void wagonService_delete() {
        Mockito.doNothing().when(wagonRepository).delete(wagon1);
        wagonService.delete(wagon1);

        Mockito.verify(wagonRepository, Mockito.times(1))
                .delete(Mockito.any(Wagon.class));
    }
}
