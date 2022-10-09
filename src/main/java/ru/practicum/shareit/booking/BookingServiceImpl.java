package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.shareit.booking.model.Booking.Status.*;
import static ru.practicum.shareit.booking.model.Booking.Status.REJECTED;
import static ru.practicum.shareit.booking.model.Booking.Status.WAITING;

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
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new NotFoundException(""));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(""));
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
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new BadRequestException(""));
        if (itemRepository.getReferenceById(booking.getItem().getId()).getOwner().getId() == userId) {
            if (booking.getStatus() == WAITING) {
                if (approved) {
                    booking.setStatus(APPROVED);
                } else booking.setStatus(REJECTED);
                bookingRepository.save(booking);
                return booking;
            } else throw new BadRequestException("Некорректный запрос ");
        } else throw new NotFoundException("Не найдено");

    }


    @Override
    public Booking find(long userId, long bookingId) throws NotFoundException {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Не найдено"));
        if (booking.getBooker().getId() == userId ||
                itemRepository.getReferenceById(booking.getItem().getId()).getOwner().getId() == userId) {
            return booking;
        } else throw new NotFoundException("Не найдено");
    }

    @Override
    public List<Booking> findAll(long userId, Booking.State state) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найдено"));
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllBookings(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentBookings(userId, now);
                break;
            case PAST:
                bookings = bookingRepository.findPastBookings(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureBookings(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByStatusBookings(userId, WAITING.name());
                break;
            case REJECTED:
                bookings = bookingRepository.findByStatusBookings(userId, REJECTED.name());
                break;
        }
        return bookings;
    }

    @Override
    public List<Booking> allUserItems(long userId, Booking.State state) throws NotFoundException {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Не найдено"));
        List<Booking> bookings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllUserItemsBookings(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findCurrentUserItemsBookings(userId, now);
                break;
            case PAST:
                bookings = bookingRepository.findPastUserItemsBookings(userId, now);
                break;
            case FUTURE:
                bookings = bookingRepository.findFutureUserItemsBookings(userId, now);
                break;
            case WAITING:
                bookings = bookingRepository.findByStatusUserItemsBookings(userId, WAITING.name());
                break;
            case REJECTED:
                bookings = bookingRepository.findByStatusUserItemsBookings(userId, REJECTED.name());
                break;
        }
        return bookings;

    }
}
