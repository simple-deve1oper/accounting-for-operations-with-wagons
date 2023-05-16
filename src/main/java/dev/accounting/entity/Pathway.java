package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "pathways")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pathway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "station_id", referencedColumnName = "id")
    private Station station;
    @Column(name = "number")
    private Integer number;

    @OneToMany(mappedBy = "pathway")
    private List<Document> documents;
}
