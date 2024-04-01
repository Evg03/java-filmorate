package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User getUser(int id);

    User updateUser(User user);

    User addUser(User user);

    List<User> getUsers();
}
