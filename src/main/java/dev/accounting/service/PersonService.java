package dev.accounting.service;

import dev.accounting.entity.Person;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person findByLogin(String login) {
        Optional<Person> person = personRepository.findByLogin(login);
        return person
                .orElseThrow(() -> new EntityNotFoundException(String.format("Пользователь с логином '%s' не найден", login)));
    }

    @Transactional
    public Person save(Person person) {
        return personRepository.save(person);
    }
}
