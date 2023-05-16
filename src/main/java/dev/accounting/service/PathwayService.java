package dev.accounting.service;

import dev.accounting.entity.Pathway;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.PathwayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PathwayService {
    @Autowired
    private PathwayRepository pathwayRepository;

    public List<Pathway> findAll() {
        List<Pathway> pathways = pathwayRepository.findAll();

        return pathways;
    }

    public Pathway findById(Long id) {
        Optional<Pathway> pathway = pathwayRepository.findById(id);

        return pathway
                .orElseThrow(() -> new EntityNotFoundException(String.format("Путь с идентификатором %d не найден", id)));
    }

    @Transactional
    public Pathway save(Pathway pathway) {
        return pathwayRepository.save(pathway);
    }

    @Transactional
    public List<Pathway> saveAll(List<Pathway> pathways) {
        List<Pathway> pathwaysFromDB = pathwayRepository.saveAll(pathways);

        return pathwaysFromDB;
    }

    @Transactional
    public void delete(Pathway station) {
        pathwayRepository.delete(station);
    }
}
