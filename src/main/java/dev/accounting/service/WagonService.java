package dev.accounting.service;

import dev.accounting.entity.Wagon;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.WagonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для класса Wagon
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class WagonService {
    @Autowired
    private WagonRepository wagonRepository;

    /**
     * Получение всех вагонов вагонов
     * @return список объектов типа Wagon
     */
    public List<Wagon> findAll() {
        List<Wagon> wagons = wagonRepository.findAll();
        return wagons;
    }

    /**
     * Получение вагона по переданному номеру
     * @param number - номер
     * @return объект типа Wagon
     */
    public Wagon findByNumber(String number) {
        Optional<Wagon> wagon = wagonRepository.findByNumber(number);
        return wagon
                .orElseThrow(() -> new EntityNotFoundException(String.format("Вагон с номером '%s' не найден", number)));
    }

    /**
     * Проверка на существование вагона по переданному номеру
     * @param number - номер
     * @return результат существования вагона
     */
    public boolean existByNumber(String number) {
        boolean result = wagonRepository.existsByNumber(number);
        return result;
    }

    /**
     * Получение идентификатора вагона по переданному номеру
     * @param number - номер
     * @return идентификатор вагона
     */
    public Long getIdByNumber(String number) {
        Long id = wagonRepository.getIdByNumber(number);
        return id;
    }

    /**
     * Сохранение вагона
     * @param wagon - объект типа Wagon
     * @return объект типа Wagon
     */
    @Transactional
    public Wagon save(Wagon wagon) {
        return wagonRepository.save(wagon);
    }

    /**
     * Удаление вагона
     * @param wagon - объект типа Wagon
     */
    @Transactional
    public void delete(Wagon wagon) {
        wagonRepository.delete(wagon);
    }
}
