package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.exception.IncorrectPathVariableException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping()
    public User addUser(@Validated(CreateGroup.class) @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping()
    public User updateUser(@Validated(UpdateGroup.class) @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public User addToFriendList(@PathVariable int id, @PathVariable int friendId) {
        if (id < 1 || friendId < 1) {
            throw new IncorrectPathVariableException("id должен быть положительным");
        }
        return userService.addToFriendList(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public User removeFromFriendList(@PathVariable int id, @PathVariable int friendId) {
        if (id < 1 || friendId < 1) {
            throw new IncorrectPathVariableException("id должен быть положительным");
        }
        return userService.removeFromFriendList(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        if (id < 1) {
            throw new IncorrectPathVariableException("id должен быть положительным");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        if (id < 1 || otherId < 1) {
            throw new IncorrectPathVariableException("id должен быть положительным");
        }
        return userService.getCommonFriends(id, otherId);
    }

}
