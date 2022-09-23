package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item create(Item item);

    Item update(Item item);

    Item find(long id);

    List<Item> findAll(long userId);

    List<Item> search(String text);

}
