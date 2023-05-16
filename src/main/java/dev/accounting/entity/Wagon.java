package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "wagons")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Wagon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "number")
    private String number;
    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private Type type;
    @Column(name = "tare_weight")
    private Double tareWeight;
    @Column(name = "load_capacity")
    private Double loadCapacity;
}
