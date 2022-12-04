package ru.practicum.shareit.booking;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ErrorResponse;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;


@Slf4j
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService service;

    @Autowired
    public BookingController(BookingService service) {
        this.service = service;
    }

    @SneakyThrows
    @PostMapping
    public Booking create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody BookingDto bookingDto) {
        log.info("Получен запрос на бронирование");
        return service.create(userId, bookingDto);
    }

    @SneakyThrows
    @PatchMapping("/{bookingId}")
    public Booking confirmationOrRejection(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @PathVariable long bookingId,
                                           @RequestParam(name = "approved", required = true) Boolean approved) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        return service.confirmationOrRejection(userId, bookingId, approved);
    }

    @SneakyThrows
    @GetMapping("/{bookingId}")
    public Booking find(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long bookingId) {
        log.info("Получение данных о конкретном бронировании");
        return service.find(userId, bookingId);
    }

    @SneakyThrows
    @GetMapping
    public List<Booking> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestParam(name = "from", defaultValue = "0") int from,
                                 @RequestParam(name = "size", defaultValue = "10") int size,
                                 @RequestParam(name = "state", defaultValue = "ALL") State state)
            throws NotFoundException, BadRequestException {
        log.info("Получение данных о всех бронированиях");
        return service.findAll(userId, from, size, state);
    }


    @GetMapping("/owner")
    public List<Booking> allUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam(name = "from", defaultValue = "0") int from,
                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                      @RequestParam(name = "state", defaultValue = "ALL") State state)
            throws NotFoundException {
        log.info("Получение списка бронирований для всех вещей текущего пользователя.");
        return service.allUserItems(userId, from, size, state);
    }


    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<ErrorResponse> handleException(ConversionFailedException exc) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ErrorResponse(status, "Unknown state: UNSUPPORTED_STATUS"), status
        );
    }
}
