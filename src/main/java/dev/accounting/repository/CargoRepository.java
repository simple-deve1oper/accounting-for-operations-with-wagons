package dev.accounting.repository;

import dev.accounting.entity.Cargo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий для класса Cargo
 * @version 1.0
 */
@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
    /**
     * Проверка на существование груза с переданным кодом
     * @param code - код
     * @return результат существования груза
     */
    boolean existsByCode(String code);
}
