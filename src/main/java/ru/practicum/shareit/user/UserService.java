package ru.practicum.shareit.user;

import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user) throws ConflictException, BadRequestException;

    UserDto update(long userId, UserDto userDto) throws NotFoundException;

    UserDto find(long id) throws NotFoundException;

    List<UserDto> findAll();

    void delete(long userId);

}
