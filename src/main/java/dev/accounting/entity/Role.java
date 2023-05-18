package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.List;

/**
 * Класс для описания роли пользователей системы
 * @version 1.0
 */
@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;                // идентификатор
    @Column(name = "name")
    private String name;            // наименование
    @Transient
    @OneToMany(mappedBy = "role")
    private List<Person> people;    // пользователи

    @Override
    public String getAuthority() {
        return String.format("ROLE_%s", name);
    }
}
