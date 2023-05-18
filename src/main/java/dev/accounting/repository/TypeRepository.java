package dev.accounting.repository;

import dev.accounting.entity.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для класса Type
 * @version 1.0
 */
@Repository
public interface TypeRepository extends JpaRepository<Type, Long> {}
