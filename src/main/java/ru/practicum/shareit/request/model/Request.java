package ru.practicum.shareit.request.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@ToString
@Entity
@Table(name = "request")
public class Request implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id; //уникальный идентификатор запроса;

    @Column(name = "description", nullable = false)
    String description; //текст запроса, содержащий описание требуемой вещи;

    @JoinColumn(name = "requestor_id")
    @ManyToOne(fetch = FetchType.EAGER)
    User requestor; //пользователь, создавший запрос;

    @Column(name = "created", nullable = false)
    LocalDateTime created;  //дата и время создания запроса.

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "request_id")
    List<Item> items;
}
