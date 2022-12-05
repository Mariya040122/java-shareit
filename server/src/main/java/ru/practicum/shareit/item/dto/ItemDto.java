package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {

    Long id;  //уникальный идентификатор вещи
    String name; // краткое название
    String description; //развёрнутое описание
    Boolean available; //статус о том, доступна или нет вещь для аренды
    Long requestId; //ссылка на запрос
}
