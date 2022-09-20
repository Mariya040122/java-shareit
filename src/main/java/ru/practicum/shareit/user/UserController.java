package ru.practicum.shareit.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }
    @SneakyThrows
    @PostMapping(value = "/users")
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя");
        return service.create(user);
    }

    @SneakyThrows
    @PatchMapping("/users/{userId}")
    public User update (@PathVariable long userId, @Valid @RequestBody UserDto user) {
        log.info("Получен запрос на изменение данных пользователя");
        return service.update(userId, user);
    }

    @SneakyThrows
    @GetMapping("/users/{id}")
    public User find (@PathVariable long id) {
        log.info("Получен запрос на вывод данных пользователя");
        return service.find(id);
    }

    @SneakyThrows
    @GetMapping("/users")
    public List<User> findAll () {
        return service.findAll();
    }

    @DeleteMapping ("/users/{userId}")
    public void delete (@PathVariable long userId){
        log.info("Получен запрос на удаление пользователя");
        service.delete(userId);
    }
}
