package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
@Component
public class UserRepositoryImpl implements UserRepository {

    List <User> users = new ArrayList<>();

    private long id = 1;

    @Override
    public User create(User user){
        if (!isEmailExist(user.getEmail(), id)) {
            user.setId(id);
            id++;
            users.add(user);
            return user;
        } else return null;
    } // создание пользователя

    @Override
    public User update (long userId, UserDto userDto){
        User user = find(userId);
        if (user != null){
            if (userDto.getEmail() != null){
                if (isEmailExist(userDto.getEmail(), userId)){
                    return null;
                } else {
                    user.setEmail(userDto.getEmail());
                }
            }
            if (userDto.getName() != null){
                user.setName(userDto.getName());
            }
        }
        return user;
    } // изменение пользователя

    @Override
    public User find (long id){
        User present = users.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        return present;
    } // вывод пользователя
    @Override
    public List<User> findAll (){
        return users;
    }
    @Override
    public void delete (long userId){
        User user = users.stream()
                .filter(c -> c.getId() == userId)
                .findFirst()
                .orElse(null);
        if (user != null) {
            users.remove(user);
        }
    } // удаление пользователя

    private boolean isEmailExist(String email, long userId){
        User present = users.stream()
                .filter(c -> c.getEmail().equals(email) && c.getId() != userId)
                .findFirst()
                .orElse(null);
        if (present != null){
            return true;
        } else return false;
    }
}
