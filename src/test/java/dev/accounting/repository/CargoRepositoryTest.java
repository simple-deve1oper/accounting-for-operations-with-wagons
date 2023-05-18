package dev.accounting.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Тестирование интерфейса CargoRepository
 * @version 1.0
 */
@DataJpaTest
public class CargoRepositoryTest {
    @Autowired
    private CargoRepository cargoRepository;

    @Test
    void cargoRepository_existsByCode() {
        boolean result = cargoRepository.existsByCode("001");
        Assertions.assertTrue(result);

        result = cargoRepository.existsByCode("123");
        Assertions.assertFalse(result);
    }
}
