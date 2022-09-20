package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemRepositoryImpl implements ItemRepository{

    List<Item> items = new ArrayList<>();

    private long id = 1;

    @Override
    public Item create (Item item){
        item.setId(id);
        id++;
        items.add(item);
        return item;
    } // создание вещи

    @Override
    public Item update (Item item){
        Item present = items.stream()
                .filter(c -> c.getId() == item.getId())
                .findAny()
                .orElse(null);
        if (present != null) {
            items.remove(present);
            items.add(item);
            return item;
        } else return null;
    } // обновление вещи

    @Override
    public Item find (long id){
        Item present = items.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        return present;
    } // получение вещи

    @Override
    public List<ItemDto> findAll (long userId){
        List<ItemDto> itemsDto = items.stream()
                .filter(c -> c.getOwner() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return itemsDto;
    } // получение всех вещей

    @Override
    public List<Item> search (String text){
        List<Item> itemsFound = items.stream()
                .filter(c -> c.getAvailable() && !text.isEmpty() &&
                        (c.getName().toLowerCase().contains(text) ||
                        c.getDescription().toLowerCase().contains(text)))
                .collect(Collectors.toList());
        return itemsFound;
    } // поиск вещи
}
