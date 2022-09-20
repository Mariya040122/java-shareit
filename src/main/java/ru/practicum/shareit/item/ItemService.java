package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {

    ItemDto create(long userId, ItemDto item) throws NotFoundException, BadRequestException;

    ItemDto update(long userId, long itemId, ItemDto item) throws NotFoundException;

    ItemDto find(long id);

    List<ItemDto> findAll(long userId);

    List<ItemDto> search(String text);
}
