package dev.accounting.security;

import dev.accounting.entity.Person;
import dev.accounting.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PersonDetailsService implements UserDetailsService {
    @Autowired
    private PersonService personService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PersonDetailsService.loadUserByUsername(String username) -> username: " + username);
        Person person = personService.findByLogin(username);
        log.info("PersonDetailsService.loadUserByUsername(String username) -> object not null: " + (person != null));
        return new PersonDetails(person);
    }
}
