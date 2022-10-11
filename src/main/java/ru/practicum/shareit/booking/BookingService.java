package ru.practicum.shareit.booking;

import ru.practicum.shareit.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;

public interface BookingService {


    Booking create(long userId, BookingDto bookingDto) throws BadRequestException, NotFoundException;


    Booking confirmationOrRejection(long userId, long bookingId, Boolean approved) throws BadRequestException, NotFoundException;


    Booking find(long userId, long bookingId) throws NotFoundException;


    List<Booking> findAll(long userId, State state) throws NotFoundException;


    List<Booking> allUserItems(long userId, State state) throws NotFoundException;

}
