package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Класс для описания документа
 * @version 1.0
 */
@Entity
@Table(name = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                        // идентификатор
    @Column(name = "serial_number")
    private Integer serialNumber;           // порядковый номер
    @Column(name = "wagon_number")
    private String wagonNumber;             // номер вагона
    @ManyToOne
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    private Cargo cargo;                    // груз
    @Column(name = "cargo_weight")
    private Double cargoWeight;             // вес груза
    @Column(name = "wagon_weight")
    private Double wagonWeight;             // вес вагона
    @ManyToOne
    @JoinColumn(name = "pathway_id", referencedColumnName = "id")
    private Pathway pathway;                // путь
    @Column(name = "departure_date")
    private LocalDateTime departureDate;    // дата убытия
}
