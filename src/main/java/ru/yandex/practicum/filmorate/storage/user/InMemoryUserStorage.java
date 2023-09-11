package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class InMemoryUserStorage implements UserStorage {
    private Map<Integer, User> users = new HashMap<>();
    private int userId;

    public User addUser(User user) {
        if (!users.containsKey(user.getId())) {
            user.setId(++userId);
            users.put(user.getId(), user);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User id = " + user.getId() + " already exists!");
        }
        return user;
    }

    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There is no user with this id = " + user.getId());
        }
    }

    public HashMap<Integer, User> getUsers() {
        return new HashMap<Integer, User>(users);
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

}
