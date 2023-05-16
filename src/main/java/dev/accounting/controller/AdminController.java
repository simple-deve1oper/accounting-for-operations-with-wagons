package dev.accounting.controller;

import dev.accounting.dto.RoleDTO;
import dev.accounting.entity.Person;
import dev.accounting.entity.Role;
import dev.accounting.service.PersonService;
import dev.accounting.service.RoleService;
import dev.accounting.util.DataUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/admin")
@Tag(name = "Методы панели администратора")
@SecurityRequirement(name = "basicAuth")
public class AdminController {
    @Autowired
    private PersonService personService;
    @Autowired
    private RoleService roleService;

    @PatchMapping("/edit/role")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Изменение роли пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    public void editRole(
            @Parameter(description = "Объект типа RoleDTO") @RequestBody @Valid RoleDTO roleDTO,
            @Parameter(hidden = true) BindingResult bindingResult
    ) {
        DataUtil.checkValidation(bindingResult);

        String role = roleDTO.getRole();
        Role roleFromDB = roleService.findByName(role);
        String login = roleDTO.getLogin();
        Person person = personService.findByLogin(login);
        person.setRole(roleFromDB);
        if (roleFromDB.getPeople().isEmpty()) {
            roleFromDB.setPeople(new ArrayList<>(){{add(person);}});
        } else {
            roleFromDB.getPeople().add(person);
        }
        personService.save(person);
    }
}
