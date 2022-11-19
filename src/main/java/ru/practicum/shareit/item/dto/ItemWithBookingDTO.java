package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemWithBookingDTO {

    Long id;  //уникальный идентификатор вещи
    String name; // краткое название
    String description; //развёрнутое описание
    Boolean available; //статус о том, доступна или нет вещь для аренды
    BookingDto lastBooking;
    BookingDto nextBooking;
    List<CommentDTO> comments;
}
