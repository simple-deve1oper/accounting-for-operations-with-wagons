package dev.accounting.repository;

import dev.accounting.entity.Wagon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WagonRepository extends JpaRepository<Wagon, Long> {
    Optional<Wagon> findByNumber(String number);
    boolean existsByNumber(String number);
    @Query("SELECT w.id FROM Wagon w WHERE w.number = :number")
    Long getIdByNumber(@Param("number") String number);
}
