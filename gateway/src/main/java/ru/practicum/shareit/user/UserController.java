package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.user.dto.CreateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserDto requestDto) {
        log.info("Создание пользователя {}", requestDto);
        return userClient.createUser(requestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable long id) {
        log.info("Получен запрос вывод информации одного пользователя");
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getUsers(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                           @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Вывод пользователей, from={}, size={}", from, size);
        return userClient.getUsers(from, size);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable long id, @RequestBody UserDto userDto)
            throws BadRequestException {
        if (userDto.getName() != null && !UserValidator.isName(userDto.getName())) {
            throw new BadRequestException("Пользовалеь не найден");
        }
        if (userDto.getEmail() != null && !UserValidator.isEmail(userDto.getEmail())) {
            throw new BadRequestException("Вещь не найдена");
        }
        log.info("Получен запрос на изменение данных пользователя");
        return userClient.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        log.info("Получен запрос на удаление пользователя");
        return userClient.deleteUser(id);
    }


}
