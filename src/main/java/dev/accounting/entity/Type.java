package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Класс для описания типа вагона
 * @version 1.0
 */
@Entity
@Table(name = "types")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;            // идентификатор
    @Column(name = "name")
    private String name;        // наименование

    @OneToMany(mappedBy = "type")
    private List<Wagon> wagons; // вагоны
}
