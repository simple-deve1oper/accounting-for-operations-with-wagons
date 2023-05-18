package dev.accounting.service;

import dev.accounting.entity.Type;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис для класса Type
 * @version 1.0
 */
@Service
@Transactional(readOnly = true)
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    /**
     * Получение типа вагона по переданному идентификатору
     * @param id - идентификатор
     * @return объект типа Type
     */
    public Type findById(Long id) {
        Optional<Type> type = typeRepository.findById(id);
        return type
                .orElseThrow(() -> new EntityNotFoundException(String.format("Тип вагона с идентификатором %d не найден", id)));
    }

    /**
     * Проверка на существование типа вагона по переданному идентификатору
     * @param id - идентификатор
     * @return результат существования типа вагона
     */
    public boolean existsById(Long id) {
        boolean result = typeRepository.existsById(id);
        return result;
    }
}
