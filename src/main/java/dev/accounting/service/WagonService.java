package dev.accounting.service;

import dev.accounting.entity.Wagon;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.WagonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class WagonService {
    @Autowired
    private WagonRepository wagonRepository;

    public List<Wagon> findAll() {
        List<Wagon> wagons = wagonRepository.findAll();
        return wagons;
    }

    public Wagon findByNumber(String number) {
        Optional<Wagon> wagon = wagonRepository.findByNumber(number);
        return wagon
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вагон с номером '%s' не найден", number)));
    }

    public boolean existByNumber(String number) {
        boolean result = wagonRepository.existsByNumber(number);
        return result;
    }

    public Long getIdByNumber(String number) {
        Long id = wagonRepository.getIdByNumber(number);
        return id;
    }

    @Transactional
    public Wagon save(Wagon wagon) {
        return wagonRepository.save(wagon);
    }

    @Transactional
    public void delete(Wagon wagon) {
        wagonRepository.delete(wagon);
    }
}
