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
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int userId;

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            validateUser(user);
            user.setId(++userId);
            users.put(user.getId(), user);
        } else {
            throw new ValidationException("User id = " + user.getId() + " already exists!");
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
            throw new ValidationException("There is no user with this id = " + user.getId());
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    private void validateUser(User user) {
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid user data!");
        } else if (user.getName() == null) {
            user.setName(user.getLogin());
        }
    }

}
