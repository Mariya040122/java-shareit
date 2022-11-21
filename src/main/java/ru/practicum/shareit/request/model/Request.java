package ru.practicum.shareit.request.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
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
@Table(name = "requests")
@AllArgsConstructor
@NoArgsConstructor
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created", nullable = false)
    LocalDateTime created;  //дата и время создания запроса.

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "request_id")
    List<Item> items;

}
