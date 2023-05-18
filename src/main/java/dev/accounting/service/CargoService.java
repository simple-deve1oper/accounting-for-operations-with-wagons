package dev.accounting.service;

import dev.accounting.entity.Cargo;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис для класса Cargo
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class CargoService {
    @Autowired
    private CargoRepository cargoRepository;

    /**
     * Получение всех грузов
     * @return список объектов типа Cargo
     */
    public List<Cargo> findAll() {
        return cargoRepository.findAll();
    }

    /**
     * Получение груза по переданному идентификатору
     * @param - идентификатор
     * @return объект типа Cargo
     */
    public Cargo findById(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Груз с идентификатором %d не найден", id)));
    }

    /**
     * Проверка на существование груза по переданному коду
     * @param code - код
     * @return результат существования груза
     */
    public boolean existsByCode(String code) {
        return cargoRepository.existsByCode(code);
    }

    /**
     * Сохранение груза
     * @param cargo - объект типа Cargo
     * @return объект типа Cargo
     */
    @Transactional
    public Cargo save(Cargo cargo) {
        return cargoRepository.save(cargo);
    }

    /**
     * Удаление груза
     * @param cargo объект типа Cargo
     */
    @Transactional
    public void delete(Cargo cargo) {
        cargoRepository.delete(cargo);
    }
}
