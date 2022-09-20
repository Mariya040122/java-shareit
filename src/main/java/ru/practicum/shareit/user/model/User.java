package ru.practicum.shareit.user.model;


import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.*;


@Getter
@Setter
public class User {

    long id; //уникальный идентификатор пользователя
    @NotBlank (message = "Имя не может быть пустым!")
    String name; //имя или логин пользователя
    @NotNull
    @Email (message = "Электронная почта не может быть пустой и должна содержать символ @!")
    String email; //адрес электронной почты


}
