package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Qualifier("userDbStorage")
    UserStorage userStorage;

    @Override
    public List<User> getUsers() {
        List<User> users = userStorage.getUsers();
        log.info("Количество пользователей = {}", users.size());
        return users;
    }

    @Override
    public User getUser(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            log.warn("Пользователя с id = {} не существует", userId);
            throw new UserNotFoundException("Пользователя с id = " + userId + " не существует");
        }
        return user;
    }

    @Override
    public User addUser(User user) {
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.info("У пользователя не задано имя, вместо имени будет использоваться логин = {}", user.getLogin());
            user.setName(user.getLogin());
        }
//        if (user.getFriends() == null) {
//            user.setFriends(new HashSet<>());
//        }
        userStorage.addUser(user);
        log.info("Пользователь с id = {} добавлен", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.getUser(user.getId()) == null) {
            log.warn("Пользователя с id = {} не существует", user.getId());
            throw new UserNotFoundException("Пользователя с id = " + user.getId() + " не существует");
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.info("У пользователя не задано имя, вместо имени будет использоваться логин = {}", user.getLogin());
            user.setName(user.getLogin());
        }
//        if (user.getFriends() == null) {
//            user.setFriends(new HashSet<>());
//        }
        userStorage.updateUser(user);
        log.info("Пользователь с id = {} обновлён", user.getId());
        return user;
    }

    @Override
    public User addToFriendList(int invitesId, int invitedId) {
        User invites = userStorage.getUser(invitesId);
        User invited = userStorage.getUser(invitedId);
        if (invites == null) {
            log.warn("Пользователя с id = {} не существует", invitesId);
            throw new UserNotFoundException("Пользователя с id = " + invitesId + " не существует");
        }
        if (invited == null) {
            log.warn("Пользователя с id = {} не существует", invitedId);
            throw new UserNotFoundException("Пользователя с id = " + invitedId + " не существует");
        }
        invites.addFriend(invited.getId());
//        invited.addFriend(invites.getId());
        userStorage.updateUser(invites);
//        userStorage.updateUser(invited);
        log.info("Пользователь с id = {} добавлен в список друзей пользователя с id = {}", invitedId, invitesId);
        return invites;
    }

    @Override
    public User removeFromFriendList(int removesId, int removedId) {
        User removes = userStorage.getUser(removesId);
        if (removes == null) {
            log.warn("Пользователя с id = {} не существует", removesId);
            throw new UserNotFoundException("Пользователя с id = " + removesId + " не существует");
        }
        User removed = userStorage.getUser(removedId);
        if (removed == null) {
            log.warn("Пользователя с id = {} не существует", removedId);
            throw new UserNotFoundException("Пользователя с id = " + removedId + " не существует");
        }
        removes.deleteFriend(removed.getId());
//        removed.deleteFriend(removes.getId());
        userStorage.updateUser(removes);
//        userStorage.updateUser(removed);
        log.info("Пользователь с id = {} удален из списка друзей пользователя с id = {}", removedId, removesId);
        return removes;
    }

    @Override
    public List<User> getFriends(int userId) {
        User user = userStorage.getUser(userId);
        if (user == null) {
            log.warn("Пользователя с id = {} не существует", userId);
            throw new UserNotFoundException("Пользователя с id = " + userId + " не существует");
        }
        List<User> friends = user.getFriends().stream().map(id -> userStorage.getUser(id)).collect(Collectors.toList());
        log.info("Количество друзей пользователя с id = {} : {}", userId, friends.size());
        return new ArrayList<>(friends);
    }

    @Override
    public List<User> getCommonFriends(int userId1, int userId2) {
        User user1 = userStorage.getUser(userId1);
        User user2 = userStorage.getUser(userId2);
        if (user1 == null) {
            log.warn("Пользователя с id = {} не существует", userId1);
            throw new UserNotFoundException("Пользователя с id = " + userId1 + " не существует");
        }
        if (user2 == null) {
            log.warn("Пользователя с id = {} не существует", userId2);
            throw new UserNotFoundException("Пользователя с id = " + userId2 + " не существует");
        }
        Set<Integer> friendList1 = new HashSet<>(user1.getFriends());
        Set<Integer> friendList2 = user2.getFriends();
        friendList1.retainAll(friendList2);
        List<User> commonFriends = friendList1.stream().map(id -> userStorage.getUser(id)).collect(Collectors.toList());
        log.info("Количество общих друзей у пользователей с id = {} и {} : {}", userId1, userId2, commonFriends.size());
        return new ArrayList<>(commonFriends);
    }

}
