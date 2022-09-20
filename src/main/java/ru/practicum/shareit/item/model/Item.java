package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
public class Item {

    long id;  //уникальный идентификатор вещи
    @NotBlank
    String name; // краткое название
    @NotBlank
    String description; //развёрнутое описание
    @NotNull
    Boolean available; //статус о том, доступна или нет вещь для аренды
    long owner;  //владелец вещи
    String request; //ссылка на запрос
}
