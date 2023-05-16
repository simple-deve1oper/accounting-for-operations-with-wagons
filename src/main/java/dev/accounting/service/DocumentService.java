package dev.accounting.service;

import dev.accounting.entity.Document;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DocumentService {
    @Autowired
    private DocumentRepository documentRepository;

    public List<Document> findAll() {
        return documentRepository.findAll();
    }

    public Document findById(Long id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Документ с идентификатором %d не найден", id)));
    }

    public boolean existsByWagonNumberAndDepartureDateIsNull(String wagonNumber) {
        return documentRepository.existsByWagonNumberAndDepartureDateIsNull(wagonNumber);
    }

    @Transactional
    public List<Document> saveAll(List<Document> documents) {
        return documentRepository.saveAll(documents);
    }

    public List<Document> findByDepartureDateIsNullAndPathway_Id(Long pathwayId, Sort sort) {
        return documentRepository.findByDepartureDateIsNullAndPathway_Id(pathwayId, sort);
    }

    public Integer maxSerialNumberByDepartureDateIsNullAndPathway_Id(Long pathwayId) {
        return documentRepository.maxSerialNumberByDepartureDateIsNullAndPathway_Id(pathwayId);
    }

    public Document findByWagonNumberAndDepartureDateIsNull(String wagonNumber) {
        return documentRepository.findByWagonNumberAndDepartureDateIsNull(wagonNumber)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вагон с номером '%s' не найден на предприятии", wagonNumber)));
    }

    public List<Document> findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(Long pathwayId, Integer limit) {
        return documentRepository.findByDepartureDateIsNullAndPathway_IdOrderBySerialNumberLimitTo(pathwayId, limit);
    }

    @Transactional
    public void delete(Document document) {
        documentRepository.delete(document);
    }
}
