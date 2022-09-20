package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item create(long userId, Item item) throws NotFoundException, BadRequestException;

    Item update (long userId, long itemId, ItemDto item) throws NotFoundException;

    Item find (long id);

    List<ItemDto> findAll (long userId);

    List<Item> search (String text);
}
