package ru.practicum.shareit.item;

import org.springframework.data.domain.Sort;
import ru.practicum.shareit.OffsetPageRequest;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithBookingDTO;
import ru.practicum.shareit.item.model.Item;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public ItemServiceImpl(ItemRepository itemRepository,
                           UserRepository userRepository,
                           BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public ItemDto create(long userId, ItemDto itemDto) throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Не найден пользователь при добавлении вещи"));
        Item item = ItemMapper.fromItemDto(itemDto);
        if (item.getName() == null || item.getName().isEmpty() ||
                item.getDescription() == null || item.getDescription().isEmpty() ||
                item.getAvailable() == null || !item.getAvailable()) {
            throw new BadRequestException("Некорректный запрос при создании вещи");
        }
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) throws NotFoundException {
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isPresent()) {
            Item foundItem = item.get();
            if (foundItem.getOwner().getId() == userId) {
                if (itemDto.getName() != null && !itemDto.getName().isEmpty()) {
                    foundItem.setName(itemDto.getName());
                }
                if (itemDto.getDescription() != null && !itemDto.getDescription().isEmpty()) {
                    foundItem.setDescription(itemDto.getDescription());
                }
                if (itemDto.getAvailable() != null) {
                    foundItem.setAvailable(itemDto.getAvailable());
                }
                itemRepository.save(foundItem);
                return ItemMapper.toItemDto(foundItem);
            } else throw new NotFoundException("Не найден такой владелец вещи");
        } else return null;
    }

    @Override
    public ItemWithBookingDTO find(long userId, long id) throws NotFoundException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Предмет не найден"));
        ItemWithBookingDTO itemDTO = ItemMapper.toItemWithBookingDTO(item);
        if (item.getOwner().getId() == userId) {
            LocalDateTime now = LocalDateTime.now();
            List<Booking> lastBookings = bookingRepository.findByItemIdAndEndBeforeOrderByEndDesc(id, now);
            if (!lastBookings.isEmpty()) {
                itemDTO.setLastBooking(BookingMapper.toBookingDto(lastBookings.get(0)));
            }
            List<Booking> nextBookings = bookingRepository.findByItemIdAndStartAfterOrderByStartAsc(id, now);
            if (!nextBookings.isEmpty()) {
                itemDTO.setNextBooking(BookingMapper.toBookingDto(nextBookings.get(0)));
            }
        }
        return itemDTO;
    }

    @Override
    public List<ItemWithBookingDTO> findAll(long userId, int from, int size) {
        List<ItemWithBookingDTO> items = itemRepository.findAllByOwnerId(userId,
                        new OffsetPageRequest(from, size, Sort.by("id").ascending())).getContent()
                .stream()
                .map(ItemMapper::toItemWithBookingDTO)
                .collect(Collectors.toList());
        for (ItemWithBookingDTO item : items) {
            LocalDateTime now = LocalDateTime.now();
            List<Booking> bookings = bookingRepository.findByItemIdAndEndBeforeOrderByEndDesc(item.getId(), now);
            if (!bookings.isEmpty()) {
                item.setLastBooking(BookingMapper.toBookingDto(bookings.get(0)));
            }
            bookings = bookingRepository.findByItemIdAndStartAfterOrderByStartAsc(item.getId(), now);
            if (!bookings.isEmpty()) {
                item.setNextBooking(BookingMapper.toBookingDto(bookings.get(0)));
            }
        }
        return items;
    }

    @Override
    public List<ItemDto> search(String text, int from, int size) {
        if (text.isEmpty()) {
            return new ArrayList<ItemDto>();
        } else return itemRepository.search(text, new OffsetPageRequest(from, size, Sort.unsorted())).getContent()
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
