package dev.accounting.service;

import dev.accounting.entity.Role;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для класса Role
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    /**
     * Получение всех ролей
     * @return список объектов типа Role
     */
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        return roles;
    }

    /**
     * Получение роли по переданному наименованию
     * @param name - наименование
     * @return объект типа Role
     */
    public Role findByName(String name) {
        Optional<Role> role = roleRepository.findByName(name);
        return role
                .orElseThrow(() -> new EntityNotFoundException(String.format("Роль с наименованием '%s' не существует", name)));
    }
}
