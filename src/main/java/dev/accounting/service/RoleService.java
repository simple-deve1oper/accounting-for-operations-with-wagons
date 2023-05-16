package dev.accounting.service;

import dev.accounting.entity.Role;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    public Role findByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        return role
                .orElseThrow(() -> new EntityNotFoundException(String.format("Роль с наименованием '%s' не существует", name)));
    }
}
