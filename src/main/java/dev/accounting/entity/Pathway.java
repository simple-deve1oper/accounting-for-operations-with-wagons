package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Класс для описания пути
 * @version 1.0
 */
@Entity
@Table(name = "pathways")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pathway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                    // идентификатор
    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    private Station station;            // станция
    @Column(name = "number")
    private Integer number;             // номер

    @OneToMany(mappedBy = "pathway")
    private List<Document> documents;   // документы
}
