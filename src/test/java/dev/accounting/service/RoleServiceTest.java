package dev.accounting.service;

import dev.accounting.entity.Role;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.RoleRepository;
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
 * Тестирование класса RoleService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleService roleService;

    private Role role1;
    private Role role2;

    @BeforeEach
    void setUp() {
        role1 = new Role(1L, "GUEST", Collections.emptyList());
        role2 = new Role(2L, "MODERATOR", Collections.emptyList());
    }

    @Test
    void roleService_findAll() {
        Mockito.when(roleRepository.findAll()).thenReturn(Arrays.asList(role1, role2));
        List<Role> roles = roleService.findAll();
        Assertions.assertNotNull(roles);
        Assertions.assertFalse(roles.isEmpty());
        Assertions.assertEquals(2, roles.size());

        Mockito.verify(roleRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    void roleService_findByName() {
        Mockito.when(roleRepository.findByName("GUEST")).thenReturn(Optional.of(role1));
        Role role = roleService.findByName("GUEST");
        Assertions.assertNotNull(role);
        Assertions.assertEquals(1L, role.getId());
        Assertions.assertEquals("GUEST", role.getName());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> roleService.findByName("TEST")
        );

        String expectedMessage = "Роль с наименованием 'TEST' не существует";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(roleRepository, Mockito.times(2))
                .findByName(Mockito.anyString());
    }

}
