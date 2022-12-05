package ru.practicum.shareit.user.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "users",
        uniqueConstraints = {@UniqueConstraint(columnNames = "email")}
)
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name")
    private String name; //имя или логин пользователя

    @Column(name = "email")
    private String email; //адрес электронной почты

}
