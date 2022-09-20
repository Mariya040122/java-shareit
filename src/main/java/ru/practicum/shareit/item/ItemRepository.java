package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository {

    Item create(Item item);

    Item update (Item item);

    Item find (long id);

    List<ItemDto> findAll (long userId);

    List<Item> search (String text);

}
