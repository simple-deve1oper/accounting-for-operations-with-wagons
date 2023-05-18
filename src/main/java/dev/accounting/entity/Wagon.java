package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс для описания вагона
 * @version 1.0
 */
@Entity
@Table(name = "wagons")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wagon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                // идентификатор
    @Column(name = "number")
    private String number;          // номер
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Type type;              // тип
    @Column(name = "tare_weight")
    private Double tareWeight;      // вес тары
    @Column(name = "load_capacity")
    private Double loadCapacity;    // грузоподъемность
}
