package dev.accounting.service;

import dev.accounting.entity.Station;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.StationRepository;
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
 * Тестирование класса StationService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class StationServiceTest {
    @Mock
    private StationRepository stationRepository;
    @InjectMocks
    private StationService stationService;

    private Station station1;
    private Station station2;

    @BeforeEach
    void setUp() {
        station1 = new Station(1L, "Станция 1", Collections.emptyList());
        station2 = new Station(2L, "Станция 2", Collections.emptyList());
    }

    @Test
    void stationService_findAll() {
        List<Station> stations = Arrays.asList(station1, station2);

        Mockito.when(stationRepository.findAll()).thenReturn(stations);
        List<Station> stationsFromDB = stationService.findAll();
        Assertions.assertNotNull(stationsFromDB);
        Assertions.assertFalse(stationsFromDB.isEmpty());
        Assertions.assertEquals(2, stationsFromDB.size());

        Mockito.verify(stationRepository, Mockito.times(1))
                .findAll();

    }

    @Test
    void stationService_findById() {
        Mockito.when(stationRepository.findById(1L))
                .thenReturn(Optional.of(station1));
        Station station = stationService.findById(1L);
        Assertions.assertNotNull(station);
        Assertions.assertEquals(1L, station.getId());
        Assertions.assertEquals("Станция 1", station.getName());
        Assertions.assertTrue(station.getPathways().isEmpty());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> stationService.findById(3L)
        );
        String expectedMessage = "Станция с идентификатором 3 не найдена";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(stationRepository, Mockito.times(2))
                .findById(Mockito.anyLong());
    }

    @Test
    void stationService_save() {
        Station station = new Station();
        station.setName("Станция 1");
        station.setPathways(Collections.emptyList());
        Mockito.when(stationRepository.save(Mockito.any(Station.class)))
                .thenReturn(station1);
        station = stationService.save(station);
        Assertions.assertNotNull(station);
        Assertions.assertEquals(1, station.getId());
        Assertions.assertEquals("Станция 1", station.getName());
        Assertions.assertTrue(station.getPathways().isEmpty());

        Mockito.verify(stationRepository, Mockito.times(1))
                .save(Mockito.any(Station.class));
    }

    @Test
    void stationService_delete() {
        Mockito.doNothing().when(stationRepository).delete(station2);
        stationService.delete(station2);

        Mockito.verify(stationRepository, Mockito.times(1))
                .delete(Mockito.any(Station.class));
    }
}
