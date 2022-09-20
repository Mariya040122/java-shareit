package ru.practicum.shareit.item;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import javax.validation.Valid;
import java.util.List;


@Slf4j
@RestController
@RequestMapping
public class ItemController {

    private final ItemService itemService;
    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }


    @SneakyThrows
    @PostMapping("/items")
    public Item create(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody Item item)
    {
        log.info("Получен запрос на добавление вещи");
        return itemService.create(userId, item);
    }

    @SneakyThrows
    @PatchMapping ("/items/{itemId}")
    public Item update (@RequestHeader("X-Sharer-User-Id")long userId, @PathVariable long itemId,
                        @Valid @RequestBody ItemDto item){
        log.info("Получен запрос на изменение данных вещи");
        return itemService.update(userId, itemId, item);
    }

    @GetMapping("/items/{itemId}")
    public Item find (@PathVariable long itemId){
        log.info("Получен запрос на получение данных о вещи");
       return itemService.find(itemId);
    }

    @GetMapping("/items")
    public List<ItemDto> findAll (@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на вывод данных о всех вещах");
        return itemService.findAll(userId);
    }

    @GetMapping("/items/search")
    public List<Item> search (@RequestParam(name = "text", required = true) String text){
        log.info("Получен запрос на поиск вещи");
    return itemService.search(text.toLowerCase());
}
}
