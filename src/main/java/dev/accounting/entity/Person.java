package dev.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Класс для описания пользователя системы
 * @version 1.0
 */
@Entity
@Table(name = "people")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;            // идентификатор
    @Column(name = "login")
    private String login;       // логин
    @Column(name = "password")
    private String password;    // пароль
    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;          // роль
}
