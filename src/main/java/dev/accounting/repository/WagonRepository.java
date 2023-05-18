package dev.accounting.repository;

import dev.accounting.entity.Wagon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для класса Wagon
 * @version 1.0
 */
@Repository
public interface WagonRepository extends JpaRepository<Wagon, Long> {
    /**
     * Нахождение вагона по переданному номеру
     * @param number - номер
     * @return класс-оболчка Optional
     */
    Optional<Wagon> findByNumber(String number);
    /**
     * Проверка на сушествование вагона с переданным номера
     * @param number номер
     * @return резултат существования вагонов
     */
    boolean existsByNumber(String number);
    /**
     * Получение идентификатора вагона по его номеру
     * @param number - номер
     * @return максимальный идентификатор
     */
    @Query("SELECT w.id FROM Wagon w WHERE w.number = :number")
    Long getIdByNumber(@Param("number") String number);
}
