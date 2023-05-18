package dev.accounting.service;

import dev.accounting.entity.Cargo;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.CargoRepository;
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
 * Тестирование класса CargoService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class CargoServiceTest {
    @Mock
    private CargoRepository cargoRepository;
    @InjectMocks
    private CargoService cargoService;

    private Cargo cargo1;
    private Cargo cargo2;

    @BeforeEach
    void setUp() {
        cargo1 = new Cargo(1L, "001", "Тест 1", Collections.emptyList());
        cargo2 = new Cargo(2L, "002", "Тест 2", Collections.emptyList());
    }

    @Test
    void stationService_findAll() {
        List<Cargo> cargos = Arrays.asList(cargo1, cargo2);

        Mockito.when(cargoRepository.findAll()).thenReturn(cargos);
        List<Cargo> cargosFromDB = cargoService.findAll();
        Assertions.assertNotNull(cargosFromDB);
        Assertions.assertFalse(cargosFromDB.isEmpty());
        Assertions.assertEquals(2, cargosFromDB.size());

        Mockito.verify(cargoRepository, Mockito.times(1))
                .findAll();

    }

    @Test
    void stationService_findById() {
        Mockito.when(cargoRepository.findById(1L))
                .thenReturn(Optional.of(cargo1));
        Cargo cargo = cargoService.findById(1L);
        Assertions.assertNotNull(cargo);
        Assertions.assertEquals(1L, cargo.getId());
        Assertions.assertEquals("001", cargo.getCode());
        Assertions.assertEquals("Тест 1", cargo.getName());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> cargoService.findById(3L)
        );
        String expectedMessage = "Груз с идентификатором 3 не найден";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(cargoRepository, Mockito.times(2))
                .findById(Mockito.anyLong());
    }

    @Test
    void stationService_existsByCode() {
        Mockito.when(cargoRepository.existsByCode(Mockito.anyString())).thenReturn(true);
        boolean result = cargoService.existsByCode("123");
        Assertions.assertTrue(result);
    }

    @Test
    void stationService_save() {
        Cargo cargo = new Cargo();
        cargo.setCode("001");
        cargo.setName("Тест 1");
        Mockito.when(cargoRepository.save(Mockito.any(Cargo.class)))
                .thenReturn(cargo1);
        cargo = cargoService.save(cargo);
        Assertions.assertNotNull(cargo);
        Assertions.assertEquals(1L, cargo.getId());
        Assertions.assertEquals("001", cargo.getCode());
        Assertions.assertEquals("Тест 1", cargo.getName());

        Mockito.verify(cargoRepository, Mockito.times(1))
                .save(Mockito.any(Cargo.class));
    }

    @Test
    void stationService_delete() {
        Mockito.doNothing().when(cargoRepository).delete(cargo2);
        cargoService.delete(cargo2);

        Mockito.verify(cargoRepository, Mockito.times(1))
                .delete(Mockito.any(Cargo.class));
    }
}
