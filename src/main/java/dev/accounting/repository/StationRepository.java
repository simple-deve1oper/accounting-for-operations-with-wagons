package dev.accounting.repository;

import dev.accounting.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для класса Station
 * @version 1.0
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
}
