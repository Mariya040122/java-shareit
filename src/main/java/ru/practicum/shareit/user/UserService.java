package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    User create (User user) throws Exception;

    User update (long userId, UserDto userDto) throws Exception;

    User find (long id);

    List<User> findAll ();

    void delete (long userId);

}
