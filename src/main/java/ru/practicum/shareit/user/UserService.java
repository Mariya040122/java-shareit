package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto create(UserDto user) throws Exception;

    UserDto update(long userId, UserDto userDto) throws Exception;

    UserDto find(long id);

    List<UserDto> findAll();

    void delete(long userId);

}
