package dev.accounting.repository;

import dev.accounting.entity.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для класса Document
 * @version 1.0
 */
@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    /**
     * Нахождение документа с информацией о вагоне с заданным номером, который сейчас находится на предприятии
     * @param wagonNumber - номер вагона
     * @return класс-оболочка Optional
     */
    Optional<Document> findByWagonNumberAndDepartureDateIsNull(String wagonNumber);

    /**
     * Проверка на существование вагона на предприятии с заданным номером
     * @param wagonNumber - номер вагона
     * @return результат проверки
     */
    boolean existsByWagonNumberAndDepartureDateIsNull(String wagonNumber);

    /**
     * Нахождение документов о вагонах в отсортированном виде по определенному полю, которые сейчас находятся на предприятии по определенному пути
     * @param pathwayId - идентификатор пути
     * @param sort - объект сортировки
     * @return список документов о вагонах
     */
    List<Document> findByDepartureDateIsNullAndPathway_Id(Long pathwayId, Sort sort);

    /**
     * Получение максимального порядкового числа вагона по определенному пути
     * @param pathway_id - идентификатор пути
     * @return максимальное порядковое число вагона
     */
    @Query(value = "SELECT max(d.serialNumber) FROM Document d WHERE d.pathway.id = :pathway_id AND d.departureDate IS NULL")
    Integer maxSerialNumberByDepartureDateIsNullAndPathway_Id(@Param("pathway_id") Long pathway_id);

    /**
     * Нахождение документов о вагонах, которые сейчас находятся на предприятии, в отсортированном виде по порядковому номеру вагона и в определенном количестве
     * @param pathway_id - идентификатор пути
     * @param limit - лимит записей для нахождения
     * @return список документов о вагонах в заданном количестве
     */
    @Query(value = "SELECT * FROM documents d WHERE d.pathway_id = :pathway_id AND d.departure_date IS NULL ORDER BY d.serial_number ASC LIMIT :limit", nativeQuery = true)
    List<Document> findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(@Param("pathway_id") Long pathway_id, @Param("limit") Integer limit);
}
