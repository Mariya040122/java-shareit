package ru.practicum.shareit.item.dto;

import lombok.*;


/**
 * TODO Sprint add-controllers.
 */

@Getter
@Setter
public class ItemDto {

    long id;  //уникальный идентификатор вещи
    String name; // краткое название
    String description; //развёрнутое описание
    Boolean available; //статус о том, доступна или нет вещь для аренды
}
