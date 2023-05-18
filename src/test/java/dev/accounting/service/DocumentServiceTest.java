package dev.accounting.service;

import dev.accounting.entity.Cargo;
import dev.accounting.entity.Document;
import dev.accounting.entity.Pathway;
import dev.accounting.entity.Station;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.DocumentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Тестирование класса DocumentService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class DocumentServiceTest {
    @Mock
    private DocumentRepository documentRepository;
    @InjectMocks
    private DocumentService documentService;

    private Document document1;
    private Document document2;

    @BeforeEach
    void setUp() {
        Station station = new Station(123L, "Станция 1", Collections.emptyList());
        Pathway pathway1 = new Pathway(23L, station, 1, Collections.emptyList());
        //station.setPathways(Collections.singletonList(pathway1));

        Cargo cargo = new Cargo(1L, "001", "Груз 1", Collections.emptyList());
        document1 = new Document(1L, 2, "1234567", cargo, 12.0, 13.0, pathway1, null);
        document2 = new Document(2L, 1, "3333223", cargo, 21.1, 33.3, pathway1, null);
        //cargo.setDocuments(Arrays.asList(document1, document2));
        //pathway1.setDocuments(Arrays.asList(document1, document2));
    }

    @Test
    void documentService_findAll() {
        List<Document> documents = Arrays.asList(document1, document2);
        Mockito.when(documentRepository.findAll()).thenReturn(documents);
        List<Document> documentsFromDB = documentService.findAll();
        Assertions.assertNotNull(documentsFromDB);
        Assertions.assertFalse(documentsFromDB.isEmpty());
        Assertions.assertEquals(2, documentsFromDB.size());
        Assertions.assertEquals(2, documentsFromDB.get(0).getSerialNumber());
        Assertions.assertEquals(1, documentsFromDB.get(1).getSerialNumber());
    }

    @Test
    void documentService_findById() {
        Mockito.when(documentRepository.findById(1L))
                .thenReturn(Optional.of(document1));
        Document document = documentService.findById(1L);
        Assertions.assertNotNull(document);
        Assertions.assertEquals(1L, document.getId());
        Assertions.assertEquals(2, document.getSerialNumber());
        Assertions.assertEquals("1234567", document.getWagonNumber());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> documentService.findById(3L)
        );
        String expectedMessage = "Документ с идентификатором 3 не найден";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void documentService_existsByWagonNumberAndDepartureDateIsNull() {
        Mockito.when(documentRepository.existsByWagonNumberAndDepartureDateIsNull("1234567"))
                .thenReturn(true);
        boolean result = documentService.existsByWagonNumberAndDepartureDateIsNull("1234567");
        Assertions.assertTrue(result);
    }

    @Test
    void documentService_saveAll() {
        List<Document> documents = Arrays.asList(document1, document2);

        Station tempStation = new Station(123L, "Станция 1", Collections.emptyList());
        Pathway tempPathway1 = new Pathway(23L, tempStation, 1, Collections.emptyList());
        tempStation.setPathways(Collections.singletonList(tempPathway1));

        Cargo tempCargo = new Cargo(1L, "001", "Груз 1", Collections.emptyList());
        Document tempDocument1 = new Document();
        tempDocument1.setWagonNumber("1234567");
        tempDocument1.setCargo(tempCargo);
        tempDocument1.setCargoWeight(12.0);
        tempDocument1.setWagonWeight(13.0);
        tempDocument1.setPathway(tempPathway1);
        Document tempDocument2 = new Document();
        tempDocument1.setWagonNumber("3333223");
        tempDocument1.setCargo(tempCargo);
        tempDocument1.setCargoWeight(21.1);
        tempDocument1.setWagonWeight(33.1);
        tempDocument1.setPathway(tempPathway1);
        tempCargo.setDocuments(Arrays.asList(tempDocument1, tempDocument2));
        tempPathway1.setDocuments(Arrays.asList(tempDocument1, tempDocument2));
        List<Document> tempDocuments = Arrays.asList(tempDocument1, tempDocument2);

        Mockito.when(documentRepository.saveAll(Mockito.anyList()))
                .thenReturn(documents);

        List<Document> result = documentRepository.saveAll(tempDocuments);
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(2, documents.get(0).getSerialNumber());
        Assertions.assertEquals(1, documents.get(1).getSerialNumber());
    }

    @Test
    void documentService_findByDepartureDateIsNullAndPathway_Id() {
        List<Document> documents = Arrays.asList(document2, document1);
        Mockito.when(documentRepository.findByDepartureDateIsNullAndPathway_Id(Mockito.anyLong(), Mockito.any(Sort.class)))
                .thenReturn(documents);
        List<Document> documentsFromDB = documentService
                .findByDepartureDateIsNullAndPathway_Id(23L, Sort.by("serialNumber"));
        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(2, documentsFromDB.size());
        Assertions.assertEquals(1, documentsFromDB.get(0).getSerialNumber());
        Assertions.assertEquals(2, documentsFromDB.get(1).getSerialNumber());
    }

    @Test
    void documentService_maxSerialNumberByDepartureDateIsNullAndPathway_Id() {
        Mockito.when(documentRepository.maxSerialNumberByDepartureDateIsNullAndPathway_Id(Mockito.anyLong()))
                .thenReturn(3);
        Integer maxNumber = documentService.maxSerialNumberByDepartureDateIsNullAndPathway_Id(23L);
        Assertions.assertNotNull(maxNumber);
        Assertions.assertEquals(3, maxNumber);
    }

    @Test
    void documentService_findByWagonNumberAndDepartureDateIsNull() {
        Mockito.when(documentRepository.findByWagonNumberAndDepartureDateIsNull("1234567"))
                .thenReturn(Optional.of(document1));
        Document document = documentService.findByWagonNumberAndDepartureDateIsNull("1234567");
        Assertions.assertNotNull(document);
        Assertions.assertEquals(2, document.getSerialNumber());
        Assertions.assertEquals("1234567", document.getWagonNumber());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> documentService.findByWagonNumberAndDepartureDateIsNull("1111111")
        );
        String expectedMessage = "Вагон с номером '1111111' не найден на предприятии";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

    }

    @Test
    void documentService_findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo() {
        Mockito.when(documentRepository.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(Mockito.anyLong(), Mockito.anyInt()))
                .thenReturn(Collections.singletonList(document2));
        List<Document> documents = documentService.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(23L, 1);
        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(1, documents.size());
        Assertions.assertEquals(1, documents.get(0).getSerialNumber());
    }

    @Test
    void documentService_delete() {
        Mockito.doNothing().when(documentRepository).delete(document2);
        documentService.delete(document2);
    }
}
