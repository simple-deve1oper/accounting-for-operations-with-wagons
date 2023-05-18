package dev.accounting.service;

import dev.accounting.entity.Person;
import dev.accounting.entity.Role;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.PersonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

/**
 * Тестирование класса PersonService
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;

    private Person person;

    @BeforeEach
    void setUp() {
        Role role = new Role(3L, "ADMIN", Collections.emptyList());
        person = new Person(34L, "admin", "1111", role);
        role.setPeople(Collections.singletonList(person));
    }

    @Test
    void personService_findByLogin() {
        Mockito.when(personRepository.findByLogin("admin")).thenReturn(Optional.of(person));
        String login = "admin";
        Person personFromDB = personService.findByLogin(login);
        Assertions.assertNotNull(personFromDB);
        Assertions.assertEquals(34, personFromDB.getId());
        Assertions.assertEquals("admin", personFromDB.getLogin());
        Assertions.assertEquals("1111", personFromDB.getPassword());
        Assertions.assertEquals(3, personFromDB.getRole().getId());
        Assertions.assertEquals("ADMIN", personFromDB.getRole().getName());

        EntityNotFoundException exception = Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> personService.findByLogin("test")
        );

        String expectedMessage = "Пользователь с логином 'test' не найден";
        String actualMessage = exception.getMessage();
        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        Mockito.verify(personRepository, Mockito.times(2))
                .findByLogin(Mockito.anyString());
    }

    @Test
    void personService_save() {
        Role role = new Role(3L, "ADMIN", Collections.emptyList());
        Person admin = new Person();
        admin.setLogin("admin");
        admin.setPassword("1111");
        admin.setRole(role);

        Mockito.when(personRepository.save(Mockito.any(Person.class)))
                .thenReturn(person);
        Person adminFromDB = personService.save(admin);
        Assertions.assertNotNull(adminFromDB);
        Assertions.assertEquals(34, adminFromDB.getId());
        Assertions.assertEquals("admin", adminFromDB.getLogin());
        Assertions.assertEquals("1111", adminFromDB.getPassword());
        Assertions.assertEquals(3, adminFromDB.getRole().getId());
        Assertions.assertEquals("ADMIN", adminFromDB.getRole().getName());

        Mockito.verify(personRepository, Mockito.times(1))
                .save(Mockito.any(Person.class));
    }
}
