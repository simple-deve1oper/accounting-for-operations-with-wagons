package dev.accounting.security;

import dev.accounting.entity.Person;
import dev.accounting.entity.Role;
import dev.accounting.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
public class PersonDetailsServiceTest {
    @Mock
    private PersonService personService;
    @InjectMocks
    private PersonDetailsService personDetailsService;

    @Test
    void personDetailsService_loadUserByUsername() {
        Role role = new Role(1L, "GUEST", Collections.emptyList());
        Person alice = new Person(56L, "alice", "1234", role);
        role.setPeople(Collections.singletonList(alice));

        Mockito.when(personService.findByLogin("alice")).thenReturn(alice);
        UserDetails userDetails = personDetailsService.loadUserByUsername("alice");
        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals("ROLE_GUEST", userDetails.getAuthorities().stream().findAny().get().getAuthority());
        Assertions.assertEquals("1234", userDetails.getPassword());
        Assertions.assertEquals("alice", userDetails.getUsername());

        Mockito.verify(personService, Mockito.times(1))
                .findByLogin(Mockito.anyString());
    }
}
