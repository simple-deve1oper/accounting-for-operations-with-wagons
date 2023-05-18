package dev.accounting.service;

import dev.accounting.entity.Document;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для класса Document
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    /**
     * Получение всех документов
     * @return список объектов типа Document
     */
    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    /**
     * Получение документа по переданному идентификатору
     * @param id - идентификатор
     * @return объект типа Document
     */
    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Документ с идентификатором %d не найден", id)));
    }

    /**
     * Проверка на существование вагона на предприятии с заданным номером
     * @param wagonNumber - номер вагона
     * @return результат существования вагона на предприятии
     */
    public boolean existsByWagonNumberAndDepartureDateIsNull(String wagonNumber) {
        return documentRepository.existsByWagonNumberAndDepartureDateIsNull(wagonNumber);
    }

    /**
     * Сохранение списка документов
     * @param documents - список объектов типа Document
     * @return список объектов типа Document
     */
    @Transactional
    public List<Document> saveAll(List<Document> documents) {
        return documentRepository.saveAll(documents);
    }

    /**
     * Нахождение документов о вагонах в отсортированном виде по определенному полю, которые сейчас находятся на предприятии по определенному пути
     * @param pathwayId - идентификатор пути
     * @param sort - объект сортировки
     * @return список документов о вагонах
     */
    public List<Document> findByDepartureDateIsNullAndPathway_Id(Long pathwayId, Sort sort) {
        return documentRepository.findByDepartureDateIsNullAndPathway_Id(pathwayId, sort);
    }

    /**
     * Получение максимального порядкового числа вагона по определенному пути
     * @param pathwayId - идентификатор пути
     * @return максимальное порядковое число вагона
     */
    public Integer maxSerialNumberByDepartureDateIsNullAndPathway_Id(Long pathwayId) {
        return documentRepository.maxSerialNumberByDepartureDateIsNullAndPathway_Id(pathwayId);
    }

    /**
     * Нахождение документа с информацией о вагоне с заданным номером, который сейчас находится на предприятии
     * @param wagonNumber - номер вагона
     * @return объект типа Document
     */
    public Document findByWagonNumberAndDepartureDateIsNull(String wagonNumber) {
        return documentRepository.findByWagonNumberAndDepartureDateIsNull(wagonNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вагон с номером '%s' не найден на предприятии", wagonNumber)));
    }

    /**
     * Нахождение документов о вагонах, которые сейчас находятся на предприятии, в отсортированном виде по порядковому номеру вагона и в определенном количестве
     * @param pathwayId - идентификатор пути
     * @param limit - лимит записей для нахождения
     * @return список объектов типа Document в заданном количестве
     */
    public List<Document> findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(Long pathwayId, Integer limit) {
        return documentRepository.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(pathwayId, limit);
    }

    /**
     * Удаление документа
     * @param document - объект типа Document
     */
    @Transactional
    public void delete(Document document) {
        documentRepository.delete(document);
    }
}
