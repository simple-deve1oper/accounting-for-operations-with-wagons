package dev.accounting.repository;

import dev.accounting.entity.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class DocumentRepositoryTest {
    @Autowired
    private DocumentRepository documentRepository;

    @Test
    void documentRepository_findByWagonNumberAndDepartureDateIsNull() {
        Optional<Document> document = documentRepository.findByWagonNumberAndDepartureDateIsNull("2222222");
        Assertions.assertTrue(document.isPresent());
        Assertions.assertEquals(4L, document.get().getId());
        Assertions.assertEquals("2222222", document.get().getWagonNumber());

        document = documentRepository.findByWagonNumberAndDepartureDateIsNull("7777777");
        Assertions.assertFalse(document.isPresent());
    }

    @Test
    void documentRepository_existsByWagonNumberAndDepartureDateIsNull() {
        boolean result = documentRepository.existsByWagonNumberAndDepartureDateIsNull("2222222");
        Assertions.assertTrue(result);

        result = documentRepository.existsByWagonNumberAndDepartureDateIsNull("7777777");
        Assertions.assertFalse(result);
    }

    @Test
    void documentRepository_findByDepartureDateIsNullAndPathway_Id() {
        List<Document> documents = documentRepository
                .findByDepartureDateIsNullAndPathway_Id(4L, Sort.by("serialNumber"));
        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(3, documents.size());

        Assertions.assertEquals(1, documents.get(0).getSerialNumber());
        Assertions.assertEquals("3333333", documents.get(0).getWagonNumber());
        Assertions.assertEquals(4L, documents.get(0).getPathway().getId());
        Assertions.assertEquals(2, documents.get(1).getSerialNumber());
        Assertions.assertEquals("1111111", documents.get(1).getWagonNumber());
        Assertions.assertEquals(4L, documents.get(1).getPathway().getId());
        Assertions.assertEquals(3, documents.get(2).getSerialNumber());
        Assertions.assertEquals("1111122", documents.get(2).getWagonNumber());
        Assertions.assertEquals(4L, documents.get(2).getPathway().getId());
    }

    @Test
    void documentRepository_maxSerialNumberByDepartureDateIsNullAndPathway_Id() {
        Integer maxNumber = documentRepository.maxSerialNumberByDepartureDateIsNullAndPathway_Id(4L);
        Assertions.assertNotNull(maxNumber);
        Assertions.assertEquals(3, maxNumber);

        maxNumber = documentRepository.maxSerialNumberByDepartureDateIsNullAndPathway_Id(1L);
        Assertions.assertNull(maxNumber);
    }

    @Test
    void documentRepository_findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo() {
        List<Document> documents = documentRepository.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(4L, 2);

        Assertions.assertNotNull(documents);
        Assertions.assertFalse(documents.isEmpty());
        Assertions.assertEquals(2, documents.size());

        Assertions.assertEquals(1, documents.get(0).getSerialNumber());
        Assertions.assertEquals("3333333", documents.get(0).getWagonNumber());
        Assertions.assertEquals(4L, documents.get(0).getPathway().getId());
        Assertions.assertEquals(2, documents.get(1).getSerialNumber());
        Assertions.assertEquals("1111111", documents.get(1).getWagonNumber());
        Assertions.assertEquals(4L, documents.get(1).getPathway().getId());
    }
}
