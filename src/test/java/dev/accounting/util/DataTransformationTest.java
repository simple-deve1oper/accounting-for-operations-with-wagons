package dev.accounting.util;

import dev.accounting.dto.*;
import dev.accounting.entity.*;
import dev.accounting.service.CargoService;
import dev.accounting.service.PathwayService;
import dev.accounting.service.StationService;
import dev.accounting.service.TypeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class DataTransformationTest {
    @Mock
    private TypeService typeService;
    @Mock
    private StationService stationService;
    @Mock
    private CargoService cargoService;
    @Mock
    private PathwayService pathwayService;

    @Test
    void dataTransformation_convertingTypeDataFromEntityToDTO() {
        Type type = new Type(1L, "Тип 1", Collections.emptyList());
        TypeDTO typeDTO = DataTransformation.convertingTypeDataFromEntityToDTO(type);
        Assertions.assertNotNull(type);
        Assertions.assertEquals(1L, typeDTO.getId());
        Assertions.assertEquals("Тип 1", typeDTO.getName());
    }

    @Test
    void dataTransformation_convertingWagonDataFromEntityToDTO() {
        Type type = new Type(1L, "Тип 1", Collections.emptyList());
        Wagon wagon = new Wagon(123L, "1234567", type, 1.1, 2.2);
        type.setWagons(Collections.singletonList(wagon));
        WagonDTO wagonDTO = DataTransformation.convertingWagonDataFromEntityToDTO(wagon);
        Assertions.assertNotNull(wagonDTO);
        Assertions.assertEquals(123L, wagonDTO.getId());
        Assertions.assertEquals("1234567", wagonDTO.getNumber());
        Assertions.assertNotNull(wagonDTO.getType());
        Assertions.assertEquals(1L, wagonDTO.getType().getId());
        Assertions.assertEquals("Тип 1", wagonDTO.getType().getName());
        Assertions.assertEquals(1.1, wagonDTO.getTareWeight());
        Assertions.assertEquals(2.2, wagonDTO.getLoadCapacity());
    }

    @Test
    void dataTransformation_convertingListDataWagonsFromEntityToDTO() {
        Type type = new Type(1L, "Тип 1", Collections.emptyList());
        Wagon wagon1 = new Wagon(123L, "1234567", type, 1.1, 2.2);
        Wagon wagon2 = new Wagon(155L, "7654321", type, 23.1, 35.5);
        List<WagonDTO> wagonsDTO = DataTransformation.convertingListDataWagonsFromEntityToDTO(Arrays.asList(wagon1, wagon2));
        Assertions.assertNotNull(wagonsDTO);
        Assertions.assertFalse(wagonsDTO.isEmpty());
        Assertions.assertEquals(2, wagonsDTO.size());
    }

    @Test
    void dataTransformation_convertingWagonDataFromDTOToEntity() {
        Type type = new Type(1L, "Тип 1", Collections.emptyList());
        Mockito.when(typeService.findById(Mockito.anyLong())).thenReturn(type);
        WagonDTO wagonDTO = new WagonDTO("1234567", new TypeDTO(1L, "Тип 1"), 1.1, 2.2);
        Wagon wagon = DataTransformation.convertingWagonDataFromDTOToEntity(wagonDTO, typeService);
        Assertions.assertNotNull(wagon);
        Assertions.assertNull(wagon.getId());
        Assertions.assertEquals("1234567", wagon.getNumber());
        Assertions.assertNotNull(wagon.getType());
        Assertions.assertEquals(1, wagon.getType().getId());
        Assertions.assertEquals("Тип 1", wagon.getType().getName());
        Assertions.assertEquals(1.1, wagon.getTareWeight());
        Assertions.assertEquals(2.2, wagon.getLoadCapacity());

        Mockito.verify(typeService, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void dataTransformation_convertingPathwayFromEntityToModelDTO() {
        Station station = new Station(1L, "Тест", Collections.emptyList());
        Pathway pathway = new Pathway(1L, station, 45, Collections.emptyList());
        station.setPathways(Collections.singletonList(pathway));

        PathwayModelDTO pathwayModelDTO = DataTransformation.convertingPathwayFromEntityToModelDTO(pathway);
        Assertions.assertNotNull(pathwayModelDTO);
        Assertions.assertEquals(1, pathwayModelDTO.getId());
        Assertions.assertEquals(45, pathwayModelDTO.getNumber());
    }

    @Test
    void dataTransformation_convertingStationFromEntityToModelDTO() {
        Station station = new Station(23L, "Тест", Collections.emptyList());
        List<Pathway> pathways = Arrays.asList(
                new Pathway(45L, station, 1, Collections.emptyList()),
                new Pathway(46L, station, 2, Collections.emptyList())
        );
        station.setPathways(pathways);

        StationModelDTO stationModelDTO = DataTransformation.convertingStationFromEntityToModelDTO(station);
        Assertions.assertNotNull(stationModelDTO);
        Assertions.assertEquals(23, stationModelDTO.getId());
        Assertions.assertEquals("Тест", stationModelDTO.getName());
        Assertions.assertEquals(45, stationModelDTO.getPathways().get(0).getId());
        Assertions.assertEquals(1, stationModelDTO.getPathways().get(0).getNumber());
        Assertions.assertEquals(46, stationModelDTO.getPathways().get(1).getId());
        Assertions.assertEquals(2, stationModelDTO.getPathways().get(1).getNumber());
    }

    @Test
    void dataTransformation_convertingPathwayFromModelDTOToEntity() {
        PathwayModelDTO pathwayModelDTO = new PathwayModelDTO(34L, 1);

        Pathway pathway = DataTransformation.convertingPathwayFromModelDTOToEntity(pathwayModelDTO);
        Assertions.assertNotNull(pathway);
        Assertions.assertEquals(34, pathway.getId());
        Assertions.assertNull(pathway.getStation());
        Assertions.assertEquals(1, pathway.getNumber());
    }

    @Test
    void dataTransformation_convertingStationFromModelDTOToEntity() {
        PathwayModelDTO pathwayModelDTO = new PathwayModelDTO(34L, 1);
        StationModelDTO stationModelDTO = new StationModelDTO(56L, "Тест", Collections.singletonList(pathwayModelDTO));

        Station station = DataTransformation.convertingStationFromModelDTOToEntity(stationModelDTO);
        Assertions.assertNotNull(station);
        Assertions.assertEquals(56, station.getId());
        Assertions.assertEquals("Тест", station.getName());
        Assertions.assertNotNull(station.getPathways());
        Assertions.assertEquals(34, station.getPathways().get(0).getId());
        Assertions.assertEquals(1, station.getPathways().get(0).getNumber());
    }

    @Test
    void dataTransformation_convertingStationDataFromEntityToDTO() {
        Station station = new Station(23L, "Тест", Collections.emptyList());
        List<Pathway> pathways = Arrays.asList(
                new Pathway(45L, station, 1, Collections.emptyList()),
                new Pathway(46L, station, 2, Collections.emptyList())
        );
        station.setPathways(pathways);

        StationDTO stationDTO = DataTransformation.convertingStationDataFromEntityToDTO(station);
        Assertions.assertNotNull(stationDTO);
        Assertions.assertEquals(23, stationDTO.getId());
        Assertions.assertEquals("Тест", stationDTO.getName());
    }

    @Test
    void dataTransformation_convertingPathwayDataFromDTOToEntity() {
        Station station = new Station(1L, "Станция 1", Collections.emptyList());
        Mockito.when(stationService.findById(1L)).thenReturn(station);

        StationDTO stationDTO = new StationDTO(1L, "Станция 1");
        PathwayDTO pathwayDTO = new PathwayDTO(1L, stationDTO, 12);

        Pathway pathway = DataTransformation.convertingPathwayDataFromDTOToEntity(pathwayDTO, stationService);
        Assertions.assertNotNull(pathway);
        Assertions.assertEquals(1L, pathway.getId());
        Assertions.assertNotNull(pathway.getStation());
        Assertions.assertEquals(12, pathway.getNumber());
    }

    @Test
    void dataTransformation_convertingPathwayDataFromEntityToDTO() {
        Station station = new Station(32L, "Тест", Collections.emptyList());
        Pathway pathway = new Pathway(56L, station, 12, Collections.emptyList());
        station.setPathways(Collections.singletonList(pathway));

        PathwayDTO pathwayDTO = DataTransformation.convertingPathwayDataFromEntityToDTO(pathway);
        Assertions.assertNotNull(pathwayDTO);
        Assertions.assertEquals(56, pathwayDTO.getId());
        Assertions.assertNotNull(pathwayDTO.getStation());
        Assertions.assertEquals(32, pathwayDTO.getStation().getId());
        Assertions.assertEquals("Тест", pathwayDTO.getStation().getName());
        Assertions.assertEquals(12, pathwayDTO.getNumber());
    }

    @Test
    void dataTransformation_convertingCargoDataFromEntityToDTO() {
        Cargo cargo = new Cargo(1L, "001", "Тест", Collections.emptyList());

        CargoDTO cargoDTO = DataTransformation.convertingCargoDataFromEntityToDTO(cargo);
        Assertions.assertNotNull(cargoDTO);
        Assertions.assertEquals(1L, cargoDTO.getId());
        Assertions.assertEquals("001", cargoDTO.getCode());
        Assertions.assertEquals("Тест", cargoDTO.getName());
    }

    @Test
    void dataTransformation_convertingCargoDataFromDTOToEntity() {
        CargoDTO cargoDTO = new CargoDTO(1L, "001", "Тест");

        Cargo cargo = DataTransformation.convertingCargoDataFromDTOToEntity(cargoDTO);
        Assertions.assertNotNull(cargo);
        Assertions.assertEquals(1L, cargo.getId());
        Assertions.assertEquals("001", cargo.getCode());
        Assertions.assertEquals("Тест", cargo.getName());
    }

    @Test
    void dataTransformation_convertingDocumentDataFromEntityToDTO() {
        Document document = new Document();
        document.setId(1L);
        document.setSerialNumber(1);
        document.setWagonNumber("1111111");
        Cargo cargo = new Cargo(1L, "001", "Тест 1", Collections.singletonList(document));
        document.setCargo(cargo);
        document.setCargoWeight(12.0);
        document.setWagonWeight(13.1);
        Station station = new Station(1L, "Станция 1", Collections.emptyList());
        Pathway pathway = new Pathway(1L, station, 1, Collections.singletonList(document));
        station.setPathways(Collections.singletonList(pathway));
        document.setPathway(pathway);

        DocumentDTO documentDTO = DataTransformation.convertingDocumentDataFromEntityToDTO(document);
        Assertions.assertNotNull(documentDTO);
        Assertions.assertEquals(1L, documentDTO.getId());
        Assertions.assertEquals(1, documentDTO.getSerialNumber());
        Assertions.assertEquals("1111111", documentDTO.getWagonNumber());
        Assertions.assertEquals(1L, documentDTO.getCargoDTO().getId());
        Assertions.assertEquals("001", documentDTO.getCargoDTO().getCode());
        Assertions.assertEquals("Тест 1", documentDTO.getCargoDTO().getName());
        Assertions.assertEquals(12.0, documentDTO.getCargoWeight());
        Assertions.assertEquals(13.1, documentDTO.getWagonWeight());
        Assertions.assertEquals(1L, documentDTO.getPathway().getId());
        Assertions.assertEquals(1, documentDTO.getPathway().getNumber());
    }

    @Test
    void dataTransformation_createListDocumentsFromReceptionDTO() {
        Station station = new Station(1L, "Станция 1", Collections.emptyList());
        Pathway pathway = new Pathway(1L, station, 1, Collections.emptyList());
        station.setPathways(Collections.singletonList(pathway));
        Cargo cargo = new Cargo(1L, "001", "Test", Collections.emptyList());

        Mockito.when(pathwayService.findById(1L)).thenReturn(pathway);
        Mockito.when(cargoService.findById(1L)).thenReturn(cargo);

        ReceptionDTO receptionDTO = new ReceptionDTO();
        WagonBasicDTO wbDTO = new WagonBasicDTO("11111111", new CargoDTO(1L, "001", "Test"), 13.0, 13.0);
        receptionDTO.setWagons(Collections.singletonList(wbDTO));
        StationDTO stationDTO = new StationDTO(1L, "Станция 1");
        PathwayDTO pathwayDTO = new PathwayDTO(1L, stationDTO, 1);
        receptionDTO.setPathway(pathwayDTO);

        List<Document> documents = DataTransformation.createListDocumentsFromReceptionDTO(receptionDTO, cargoService, pathwayService);

        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(1, documents.size());
    }
}
