package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {


    User create(User user);

    User update(long userId, User user);

    User find(long id);

    List<User> findAll();

    void delete(long userId);
}