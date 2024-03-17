package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.User.UserBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    Map<Integer, User> users = new HashMap<>();
    int idCounter = 1;

    @GetMapping()
    public List<User> getUsers() {
        log.info("Количество пользователей = " + users.size());
        return new ArrayList<>(users.values());
    }

    @PostMapping()
    public User addUser(@Validated(CreateGroup.class) @RequestBody User user) {
        UserBuilder userBuilder = user.toBuilder();
        User newUser = userBuilder.id(idCounter++).build();
        String name = user.getName();
        if (name == null || name.isBlank()) {
            log.info("У пользователя не задано имя, вместо имени будет использоваться логин = " + newUser.getLogin());
            newUser = userBuilder.name(user.getLogin()).build();
        }
        users.put(newUser.getId(), newUser);
        log.info("Пользователь с id = " + newUser.getId() + " добавлен");
        return newUser;
    }

    @PutMapping()
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) {
        if (users.get(user.getId()) == null) {
            log.warn("Пользователя с id = " + user.getId() + " не существует");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователя с id = " + user.getId() + " не существует");
        }
        users.put(user.getId(), user);
        log.info("Пользователь с id = " + user.getId() + " обновлён");
        return user;
    }
}
