package dev.accounting.service;

import dev.accounting.entity.Pathway;
import dev.accounting.entity.Station;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.PathwayRepository;
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

@ExtendWith(MockitoExtension.class)
public class PathwayServiceTest {
    @Mock
    private PathwayRepository pathwayRepository;
    @InjectMocks
    private PathwayService pathwayService;

    private Station station;
    private Pathway pathway1;
    private Pathway pathway2;

    @BeforeEach
    void setUp() {
        station = new Station(1L, "Станция 1", Collections.emptyList());
        pathway1 = new Pathway(1L, station, 1, Collections.emptyList());
        pathway2 = new Pathway(2L, station, 2, Collections.emptyList());
        station.setPathways(Arrays.asList(pathway1, pathway2));
    }

    @Test
    void pathwayService_findAll() {
        List<Pathway> pathways = Arrays.asList(pathway1, pathway2);
        Mockito.when(pathwayRepository.findAll()).thenReturn(pathways);
        List<Pathway> pathwaysFromDB = pathwayService.findAll();
        Assertions.assertNotNull(pathwaysFromDB);
        Assertions.assertFalse(pathwaysFromDB.isEmpty());
        Assertions.assertEquals(2, pathwaysFromDB.size());

        Mockito.verify(pathwayRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void pathwayService_findById() {
        Mockito.when(pathwayRepository.findById(1L))
                .thenReturn(Optional.of(pathway1));
        Pathway pathway = pathwayService.findById(1L);
        Assertions.assertNotNull(pathway);
        Assertions.assertEquals(1L, pathway.getId());
        Assertions.assertNotNull(pathway.getStation());
        Assertions.assertEquals(1, pathway.getNumber());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> pathwayService.findById(3L)
        );
        String expectedMessage = "Путь с идентификатором 3 не найден";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(pathwayRepository, Mockito.times(2))
                .findById(Mockito.anyLong());
    }

    @Test
    void pathwayService_save() {
        Pathway pathway = new Pathway();
        pathway.setStation(station);
        pathway.setNumber(1);

        Mockito.when(pathwayRepository.save(Mockito.any(Pathway.class)))
                .thenReturn(pathway1);
        pathway = pathwayService.save(pathway);
        Assertions.assertNotNull(pathway);
        Assertions.assertEquals(1L, pathway.getId());
        Assertions.assertNotNull(pathway.getStation());
        Assertions.assertEquals(1, pathway.getNumber());

        Mockito.verify(pathwayRepository, Mockito.times(1))
                .save(Mockito.any(Pathway.class));
    }

    @Test
    void pathwayService_saveAll() {
        Pathway tempPathway1 = new Pathway();
        tempPathway1.setStation(station);
        tempPathway1.setNumber(1);
        Pathway tempPathway2 = new Pathway();
        tempPathway2.setStation(station);
        tempPathway2.setNumber(1);
        List<Pathway> tempPathways = Arrays.asList(tempPathway1, tempPathway2);

        List<Pathway> pathways = Arrays.asList(pathway1, pathway2);
        Mockito.when(pathwayRepository.saveAll(Mockito.anyList()))
                .thenReturn(pathways);
        tempPathways = pathwayService.saveAll(tempPathways);
        Assertions.assertNotNull(tempPathways);
        Assertions.assertEquals(1L, tempPathways.get(0).getId());
        Assertions.assertEquals(1, tempPathways.get(0).getNumber());
        Assertions.assertEquals(2L, tempPathways.get(1).getId());
        Assertions.assertEquals(2, tempPathways.get(1).getNumber());

        Mockito.verify(pathwayRepository, Mockito.times(1))
                .saveAll(Mockito.anyList());
    }

    @Test
    void pathwayService_delete() {
        Mockito.doNothing().when(pathwayRepository).delete(pathway2);
        pathwayService.delete(pathway2);

        Mockito.verify(pathwayRepository, Mockito.times(1))
                .delete(Mockito.any(Pathway.class));
    }
}
