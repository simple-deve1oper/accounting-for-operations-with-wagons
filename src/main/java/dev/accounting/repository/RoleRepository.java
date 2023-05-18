package dev.accounting.repository;

import dev.accounting.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для класса Role
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Нахождение роли с переданным наименованием
     * @param name - наименование
     * @return класс-оболочка Optional
     */
    Optional<Role> findByName(String name);
}
