package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ConflictException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;


@Service

public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    @Autowired
    public UserServiceImpl(UserRepository repository){
        this.repository = repository;
    }

    @Override
   public User create(User user) throws ConflictException {
        User userAdded = repository.create(user);
        if (userAdded == null){
            throw new ConflictException("Конфликт, пользователь с таким Email существует");
        }
        return userAdded;
    }

    @Override
    public User update (long userId, UserDto userDto) throws ConflictException{
        User user = repository.update(userId, userDto);
        if (user == null){
                    throw new ConflictException("Конфликт, такой email существует");
        }
        return user;
    }

    @Override
    public User find (long id){
        return repository.find(id);
    }
    @Override
   public List<User> findAll (){
        return repository.findAll();
    }
    @Override
    public void delete (long userId){
        repository.delete(userId);
    }

}
