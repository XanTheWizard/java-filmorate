package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    public UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) { //Adding a new user
        validateUser(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) { //Changing user information
        validateUser(user);
        return userStorage.updateUser(user);
    }


    public List<User> getUsersList() { //list of all users
        return new ArrayList<>(userStorage.getUsers());
    }

    public User getUserById(int id) { //Getting user by id
        User user = userStorage.getUserById(id);
        if (user != null) {
            return user;
        } else {
            //throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There is no such user! Id = " + id);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "There is no such user! Id = " + id);
        }
    }

    //Adding as a friend
    public void addFriend(int idUserOne, int idUserTwo) {
        User friend = getUserById(idUserTwo);
        getUserById(idUserOne).getFriends().add(idUserTwo);
        friend.getFriends().add(idUserOne);
    }

    //Unfriending
    public void deleteFriend(int idUserOne, int idUserTwo) {
        getUserById(idUserOne).getFriends().remove(idUserTwo);
        getUserById(idUserTwo).getFriends().remove(idUserOne);
    }

    //list of users who are his friends
    public List<User> getListOfFriends(int idUserOne) {
        return setToList(getUserById(idUserOne).getFriends());
    }

    //list of friends shared with another user
    public List<User> getListOfMutualFriends(int idUserOne, int idUserTwo) {
        Set<Integer> userOneFriends = new HashSet<>(getUserById(idUserOne).getFriends());
        Set<Integer> userTwoFriends = new HashSet<>(getUserById(idUserTwo).getFriends());
        Set<Integer> mutualFriendIds = userOneFriends.stream()
                .filter(userTwoFriends::contains)
                .collect(Collectors.toSet());
        List<User> mutualFriends = mutualFriendIds.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
        return mutualFriends;
    }

    private List<User> setToList(Set<Integer> friendsFromSet) {
        return friendsFromSet.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (!user.getEmail().contains("@") || user.getLogin().contains(" ") || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Invalid user data!");
        } else if (user.getName() == null || user.getName().equals("")) {
            user.setName(user.getLogin());
        }
    }

    private <T> Set<T> mergeSet(Set<T> a, Set<T> b) {
        return new HashSet<T>() { {
                addAll(a);
                addAll(b);
            }
        };
    }

}
