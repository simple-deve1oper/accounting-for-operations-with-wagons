package dev.accounting.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.accounting.dto.RoleDTO;
import dev.accounting.entity.Person;
import dev.accounting.entity.Role;
import dev.accounting.security.PersonDetailsService;
import dev.accounting.service.PersonService;
import dev.accounting.service.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PersonService personService;
    @MockBean
    private RoleService roleService;
    @MockBean
    private BindingResult bindingResult;
    @MockBean
    private PersonDetailsService personDetailsService;

    @Test
    @WithMockUser(username = "admin", password = "1111", roles = {"ADMIN"})
    void adminController_editRole() throws Exception {
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);

        RoleDTO roleDTO = new RoleDTO("bob", "MODERATOR");
        String requestJson = objectMapper.writeValueAsString(roleDTO);

        Role guest = new Role(1L, "GUEST", Collections.emptyList());
        Person bob = new Person(45L, "bob", "5678", guest);
        guest.setPeople(Collections.singletonList(bob));

        Role moderator = new Role(2L, "MODERATOR", Collections.emptyList());

        Mockito.when(roleService.findByName("MODERATOR")).thenReturn(moderator);
        Mockito.when(personService.findByLogin("bob")).thenReturn(bob);

        mockMvc.perform(patch("/api/v1/admin/edit/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk());
    }
}
