package dev.accounting.repository;

import dev.accounting.entity.Pathway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для класса Pathway
 * @version 1.0
 */
@Repository
public interface PathwayRepository extends JpaRepository<Pathway, Long> {
}
