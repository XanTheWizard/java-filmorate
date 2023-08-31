package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private HashMap<Integer, User> users = new HashMap<>();
    private int id;
    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        if(!users.containsKey(user.getId())) {
            validateUser(user);
            ++id;
            user.setId(id);
            users.put(id, user);
        } else {
            throw new ValidationException("Invalid data");
        }
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            validateUser(user);
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("This user doesn't exist");
        }
    }

    @GetMapping("/getusers")
    public List<User> getUsers() {
        return new ArrayList<User>(users.values());
    }

    private void validateUser(User user) {
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid data");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

    public HashMap<Integer,User> getUsersMap() {
        return new HashMap<>(users);
    }

}
