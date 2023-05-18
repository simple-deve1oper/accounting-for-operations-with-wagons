package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Класс для описания груза
 * @version 1.0
 */
@Entity
@Table(name = "cargos")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Cargo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                    // идентификатор
    @Column(name = "code")
    private String code;                // код
    @Column(name = "name")
    private String name;                // наиенование

    @OneToMany(mappedBy = "cargo")
    private List<Document> documents;   // документы
}
