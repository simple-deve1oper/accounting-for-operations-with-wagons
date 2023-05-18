package dev.accounting.service;

import dev.accounting.entity.Pathway;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.PathwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для класса Pathway
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class PathwayService {
    @Autowired
    private PathwayRepository pathwayRepository;

    /**
     * Получение всех путей
     * @return список объектов типа Pathway
     */
    public List<Pathway> findAll() {
        List<Pathway> pathways = pathwayRepository.findAll();

        return pathways;
    }

    /**
     * Получение пути по заданному идентификатору
     * @param id - идентификатор
     * @return объект типа Pathway
     */
    public Pathway findById(Long id) {
        Optional<Pathway> pathway = pathwayRepository.findById(id);

        return pathway
                .orElseThrow(() -> new EntityNotFoundException(String.format("Путь с идентификатором %d не найден", id)));
    }

    /**
     * Сохранение пути
     * @param pathway - объект типа Pathway
     * @return объект типа Pathway
     */
    @Transactional
    public Pathway save(Pathway pathway) {
        return pathwayRepository.save(pathway);
    }

    /**
     * Сохранение списка путей
     * @param pathways - список объектов типа Pathway
     * @return список объектов типа Pathway
     */
    @Transactional
    public List<Pathway> saveAll(List<Pathway> pathways) {
        List<Pathway> pathwaysFromDB = pathwayRepository.saveAll(pathways);

        return pathwaysFromDB;
    }

    /**
     * Удаление пути
     * @param pathway - объект типа Pathway
     */
    @Transactional
    public void delete(Pathway pathway) {
        pathwayRepository.delete(pathway);
    }
}
