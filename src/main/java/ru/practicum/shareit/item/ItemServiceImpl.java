package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public ItemServiceImpl(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) throws NotFoundException, BadRequestException {

        User user = userRepository.find(userId);
        if (user == null) {
            throw new NotFoundException("Не найден пользователь при добавлении вещи");
        }
        Item item = ItemMapper.fromItemDto(itemDto);
        if (item.getName() == null || item.getName().isEmpty() ||
                item.getDescription() == null || item.getDescription().isEmpty() ||
                item.getAvailable() == null || !item.getAvailable()) {
            throw new BadRequestException("Некорректный запрос при создании вещи");
        }
        item.setOwner(userId);
        return ItemMapper.toItemDto(itemRepository.create(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException {
        Item item = itemRepository.find(itemId);
        if (item != null) {
            if (item.getOwner() == userId) {
                if (itemDto.getName() != null) {
                    item.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null) {
                    item.setDescription(itemDto.getDescription());
                }
                if (itemDto.getAvailable() != null) {
                    item.setAvailable(itemDto.getAvailable());
                }
                itemRepository.update(item);
            } else throw new NotFoundException("Не найден такой владелец вещи");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto find(long id) {
        return ItemMapper.toItemDto(itemRepository.find(id));
    }

    @Override
    public List<ItemDto> findAll(long userId) {
        return itemRepository.findAll(userId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
