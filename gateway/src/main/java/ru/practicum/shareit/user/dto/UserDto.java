package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    Long id; //уникальный идентификатор пользователя
    String name; //имя или логин пользователя
    String email; //адрес электронной почты
}
