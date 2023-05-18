package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Класс для описания станции
 * @version 1.0
 */
@Entity
@Table(name = "stations")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                // идентификатор
    @Column(name = "name")
    private String name;            // наименование

    @OneToMany(mappedBy = "station")
    private List<Pathway> pathways; // пути
}
