package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDto create(UserDto userDto) throws ConflictException, BadRequestException {
        User user = UserMapper.fromUserDto(userDto);
        if (!UserValidator.isName(user.getName()) || !UserValidator.isEmail(user.getEmail())) {
            throw new BadRequestException("Некорректный запрос при добавлении пользователя");
        }
        user = repository.create(user);
        if (user == null) {
            throw new ConflictException("Конфликт, пользователь с таким Email существует");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) throws ConflictException, NotFoundException {
        User user = repository.find(userId);
        User newUser = UserMapper.fromUserDto(userDto);
        if (user != null) {
            newUser.setId(user.getId());
            if (!UserValidator.isName(newUser.getName())) {
                newUser.setName(user.getName());
            }
            if (!UserValidator.isEmail(newUser.getEmail())) {
                newUser.setEmail(user.getEmail());
            }
        } else throw new NotFoundException("Пользователь не найден");
        user = repository.update(userId, newUser);
        if (user == null) {
            throw new ConflictException("Конфликт, такой email существует");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto find(long id) {
        return UserMapper.toUserDto(repository.find(id));
    }

    @Override
    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long userId) {
        repository.delete(userId);
    }

}