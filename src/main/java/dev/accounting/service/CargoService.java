package dev.accounting.service;

import dev.accounting.entity.Cargo;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CargoService {
    @Autowired
    private CargoRepository cargoRepository;

    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    public Cargo findById(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Груз с идентификатором %d не найден", id)));
    }

    public boolean existsByCode(String code) {
        return cargoRepository.existsByCode(code);
    }

    @Transactional
    public Cargo save(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    @Transactional
    public void delete(Cargo cargo) {
        cargoRepository.delete(cargo);
    }
}
