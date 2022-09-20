package ru.practicum.shareit.booking;

import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class Booking {

    long id; //уникальный идентификатор бронирования
    LocalDateTime start; //дата и время начала бронирования
    LocalDateTime end; //дата и время конца бронирования
    Item item; //вещь, которую пользователь бронирует
    User booker; //пользователь, который осуществляет бронирование
    String status; //статус бронирования

    public enum status { // статус бронирования
        WAITING,
        APPROVED,
        REJECTED,
        CANCELED
    }
}
