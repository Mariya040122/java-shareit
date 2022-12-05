package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    private final CommentClient commentClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Создание вещи {}", userId, itemDto);
        return itemClient.createItem(userId, itemDto);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на изменение данных вещи");
        return itemClient.updateItem(userId, itemId, itemDto);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                          @PathVariable long itemId) {
        log.info("Получен запрос на получение данных о вещи");
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Получен запрос на вывод данных о всех вещах");
        return itemClient.getItems(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @RequestParam(name = "text", required = true) String text,
                                             @RequestParam(name = "from", defaultValue = "0") @PositiveOrZero int from,
                                             @RequestParam(name = "size", defaultValue = "10") @Positive int size) {
        log.info("Получен запрос на поиск вещи");
        return itemClient.searchItem(userId, text.toLowerCase(), from, size);
    }


    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable long itemId,
                                                @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавление коментария");
        return commentClient.createComment(userId, itemId, commentDto);
    }
}
