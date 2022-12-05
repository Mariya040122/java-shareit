package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.State;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.exceptions.ErrorResponse;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam(name = "state", defaultValue = "ALL") State state,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get booking with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookings(userId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmationOrRejection(@RequestHeader("X-Sharer-User-Id") long userId,
                                                          @PathVariable long bookingId,
                                                          @RequestParam(name = "approved", required = true) Boolean approved) {
        log.info("Подтверждение или отклонение запроса на бронирование");
        return bookingClient.confirmationOrRejection(userId, bookingId, approved);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> allUserItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(name = "size", defaultValue = "10") @Positive int size,
                                               @RequestParam(name = "state", defaultValue = "ALL") State state) {
        log.info("Получение списка бронирований для всех вещей текущего пользователя.");
        return bookingClient.allUserItems(userId, from, size, state);
    }

    @ExceptionHandler({ConversionFailedException.class})
    public ResponseEntity<ErrorResponse> handleException(ConversionFailedException exc) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ErrorResponse(status, "Unknown state: UNSUPPORTED_STATUS"), status
        );
    }
}
