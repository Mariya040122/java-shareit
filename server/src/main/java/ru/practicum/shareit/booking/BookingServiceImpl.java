package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.OffsetPageRequest;
import ru.practicum.shareit.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.OffsetPageRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.Status.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Booking create(long userId, BookingDto bookingDto) throws BadRequestException, NotFoundException {
        Booking booking = BookingMapper.fromBookingDto(bookingDto);
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException("Вещь не найдена"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (item.getAvailable()) {
            booking.setItem(item);
            LocalDateTime now = LocalDateTime.now();
            if (item.getOwner().getId() != userId) {
                if (bookingDto.getStart().isAfter(now) && bookingDto.getEnd().isAfter(now)
                        && bookingDto.getEnd().isAfter(bookingDto.getStart())) {
                    booking.setBooker(user);
                    booking.setStatus(WAITING);
                    return bookingRepository.save(booking);
                }
                throw new BadRequestException("Некорректный запрос при бронировании вещи");
            }
            throw new NotFoundException("Не найден пользователь");
        }
        throw new BadRequestException("Некорректный запрос при бронировании вещи");
    }


    @Override
    public Booking confirmationOrRejection(long userId, long bookingId, Boolean approved) throws BadRequestException,
            NotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new BadRequestException("Бронирование ненайдено"));
        if (itemRepository.findById(booking.getItem().getId()).get().getOwner().getId() == userId) {
            if (booking.getStatus() == WAITING) {
                if (approved) {
                    booking.setStatus(APPROVED);
                } else booking.setStatus(REJECTED);
                bookingRepository.save(booking);
                return booking;
            } else throw new BadRequestException("Некорректный запрос ");
        } else throw new NotFoundException("Бронирование ненайдено");

    }

    @Override
    public Booking find(long userId, long bookingId) throws NotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() ->
                new NotFoundException("Бронирование ненайдено"));
        if (booking.getBooker().getId() == userId ||
                itemRepository.findById(booking.getItem().getId()).get().getOwner().getId() == userId) {
            return booking;
        } else throw new NotFoundException("Бронирование ненайдено");
    }

    @Override
    public List<Booking> findAll(long userId, int from, int size, State state) throws NotFoundException, BadRequestException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByBookerId(userId, new OffsetPageRequest(from, size,
                        Sort.by("start").descending())).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndStartBeforeAndEndAfter(userId, now, now,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndBefore(userId, now,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findByBookerIdAndStartAfter(userId, now,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findByBookerIdAndStatus(userId, WAITING,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(userId, REJECTED,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
        }
        return bookings;
    }

    @Override
    public List<Booking> allUserItems(long userId, int from, int size, State state) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItem_OwnerId(userId, new OffsetPageRequest(from, size,
                        Sort.by("start").descending())).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findByItem_OwnerIdAndStartBeforeAndEndAfter(userId,
                        now, now, new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findByItem_OwnerIdAndEndBefore(userId, now,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findByItem_OwnerIdAndStartAfter(userId, now,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findByItem_OwnerIdAndStatus(userId, WAITING,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findByItem_OwnerIdAndStatus(userId, REJECTED,
                        new OffsetPageRequest(from, size, Sort.by("start").descending())).getContent();
                break;
        }
        return bookings;
    }
}
