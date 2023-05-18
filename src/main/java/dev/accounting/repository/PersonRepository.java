package dev.accounting.repository;

import dev.accounting.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для класса Person
 * @version 1.0
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    /**
     * Нахождение пользователя с переданным логином
     * @param login - логин
     * @return класс-оболочка Optional
     */
    Optional<Person> findByLogin(String login);
}
