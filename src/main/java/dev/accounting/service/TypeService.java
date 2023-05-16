package dev.accounting.service;

import dev.accounting.entity.Type;
import dev.accounting.exception.EntityNotFoundException;
import dev.accounting.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    public Type findById(Long id) {
        Optional<Type> type = typeRepository.findById(id);
        return type
                .orElseThrow(() -> new EntityNotFoundException(String.format("Тип вагона с идентификатором %d не найден", id)));
    }

    public boolean existsById(Long id) {
        boolean result = typeRepository.existsById(id);
        return result;
    }
}
