package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.BadRequestException;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.request.OffsetPageRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
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
        user = repository.save(user);
        if (user == null) {
            throw new ConflictException("Конфликт, пользователь с таким Email существует");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto update(long userId, UserDto userDto) throws NotFoundException {
        User user = repository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User newUser = UserMapper.fromUserDto(userDto);
        if (user != null) {
            if (newUser.getName() != null) {
                user.setName(newUser.getName());
            }
            if (newUser.getEmail() != null) {
                user.setEmail(newUser.getEmail());
            }
        } else throw new NotFoundException("Пользователь не найден");
        user = repository.save(user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto find(long id) throws NotFoundException {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Ошибка");
        }
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public List<UserDto> findAll(Integer from, Integer size) {
        return repository.findAll(new OffsetPageRequest(from, size, Sort.by("id"))).getContent()
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long userId) {
        repository.delete(repository.getReferenceById(userId));
    }

}