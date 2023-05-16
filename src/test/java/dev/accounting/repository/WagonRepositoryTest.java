package dev.accounting.repository;

import dev.accounting.entity.Wagon;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class WagonRepositoryTest {
    @Autowired
    private WagonRepository wagonRepository;

    @Test
    void wagonRepository_findByNumber() {
        String number = "1111122";
        Optional<Wagon> wagon = wagonRepository.findByNumber(number);
        Assertions.assertNotNull(wagon);
        Assertions.assertTrue(wagon.isPresent());
        Assertions.assertEquals("1111122", wagon.get().getNumber());
        Assertions.assertNotNull(wagon.get().getType());
        Assertions.assertEquals("Тип 1", wagon.get().getType().getName());

        number = "3333444";
        wagon = wagonRepository.findByNumber(number);
        Assertions.assertNotNull(wagon);
        Assertions.assertFalse(wagon.isPresent());
    }

    @Test
    void wagonRepository_existsByNumber() {
        String number = "1111122";
        boolean result = wagonRepository.existsByNumber(number);
        Assertions.assertTrue(result);

        number = "3333444";
        result = wagonRepository.existsByNumber(number);
        Assertions.assertFalse(result);
    }

    @Test
    void wagonRepository_getIdByNumber() {
        String number = "6666677";
        Long id = wagonRepository.getIdByNumber(number);
        Assertions.assertNotNull(id);
        Assertions.assertEquals(2, id);

        number = "3333444";
        id = wagonRepository.getIdByNumber(number);
        Assertions.assertNull(id);
    }
}
