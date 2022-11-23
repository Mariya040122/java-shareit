package ru.practicum.shareit.request;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService service;

    @Autowired
    public RequestController(RequestService service) {
        this.service = service;
    }

    @SneakyThrows
    @PostMapping
    public RequestDto create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody RequestDto requestDto) {
        log.info("Получен запрос на получение конкретной вещи");
        return service.create(userId, requestDto);
    }

    @SneakyThrows
    @GetMapping
    public List<Request> find(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение списка своих запросов вместе с данными об ответах на них.");
        return service.find(userId);
    }

    @SneakyThrows
    @GetMapping("/all")
    public List<Request> findAll(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                 @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Получен запрос на получение списока запросов, созданных другими пользователями.");
        return service.findAll(userId, from, size);
    }

    @SneakyThrows
    @GetMapping("/{requestId}")
    public Request findById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long requestId) {
        log.info("Получен запрос на получение данные об одном конкретном запросе вместе с данными" +
                " об ответах на него в том же формате.");
        return service.findById(userId, requestId);
    }


}
