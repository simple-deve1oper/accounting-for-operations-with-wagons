package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "serial_number")
    private Integer serialNumber;
    @Column(name = "wagon_number")
    private String wagonNumber;
    @ManyToOne
    @JoinColumn(name = "cargo_id", referencedColumnName = "id")
    private Cargo cargo;
    @Column(name = "cargo_weight")
    private Double cargoWeight;
    @Column(name = "wagon_weight")
    private Double wagonWeight;
    @ManyToOne
    @JoinColumn(name = "pathway_id", referencedColumnName = "id")
    private Pathway pathway;
    @Column(name = "departure_date")
    private LocalDateTime departureDate;
}
