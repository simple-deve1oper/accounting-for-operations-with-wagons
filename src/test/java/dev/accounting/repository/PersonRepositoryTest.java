package dev.accounting.repository;

import dev.accounting.entity.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    void personRepository_findByLogin() {
        String login = "admin";
        Optional<Person> person = personRepository.findByLogin(login);
        Assertions.assertFalse(person.isEmpty());
        Assertions.assertEquals("admin", person.get().getLogin());
        Assertions.assertEquals(3, person.get().getRole().getId());
        Assertions.assertEquals("ADMIN", person.get().getRole().getName());

        login = "test";
        person = personRepository.findByLogin(login);
        Assertions.assertTrue(person.isEmpty());
    }
}
