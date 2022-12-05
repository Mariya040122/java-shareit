package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class CreateUserDto {
    private Long id;
    @NotNull
    @NotBlank(message = "Имя не может быть пустым!")
    private String name; //имя или логин пользователя
    @NotNull
    @Email(message = "Электронная почта не может быть пустой и должна содержать символ @!")
    private String email; //адрес электронной почты
}
