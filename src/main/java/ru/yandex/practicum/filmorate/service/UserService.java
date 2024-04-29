package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();

    User getUser(int userId);

    User addUser(User user);

    User updateUser(User user);

    User addToFriendList(int invitesId, int invitedId);

    User removeFromFriendList(int removesId, int removedId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId1, int userId2);
}
