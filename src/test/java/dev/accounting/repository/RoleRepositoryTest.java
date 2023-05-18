package dev.accounting.repository;

import dev.accounting.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

/**
 * Тестирование интерфейса RoleRepository
 * @version 1.0
 */
@DataJpaTest
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    @Test
    void roleRepository_findByName() {
        String name = "ADMIN";
        Optional<Role> role = roleRepository.findByName(name);
        Assertions.assertFalse(role.isEmpty());
        Assertions.assertEquals("ADMIN", role.get().getName());

        name = "TEST";
        role = roleRepository.findByName(name);
        Assertions.assertTrue(role.isEmpty());

    }
}
