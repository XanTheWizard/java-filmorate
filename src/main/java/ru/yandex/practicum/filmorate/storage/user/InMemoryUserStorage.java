package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalErrorException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int userId;

    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            user.setId(++userId);
            users.put(user.getId(), user);
        } else {
            //throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User id = " + user.getId() + " already exists!");
            throw new InternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "User id = " + user.getId() + " already exists!");
        }
        return user;
    }

    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            //throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no user with this id = " + user.getId());
            throw new InternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no user with this id = " + user.getId());
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

}
