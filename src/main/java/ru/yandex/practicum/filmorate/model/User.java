package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.controller.validator.HasNoWhiteSpaces;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class User {
    @Null(groups = {CreateGroup.class}, message = "id должен быть null")
    @NotNull(groups = {UpdateGroup.class}, message = "id не должен быть null")
    @Positive(groups = UpdateGroup.class, message = "id должен быть больше 0")
    Integer id;
    @Email(groups = {CreateGroup.class, UpdateGroup.class}, message = "Некорректный email")
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "Email не должен быть null")
    String email;
    @NotEmpty(groups = {CreateGroup.class, UpdateGroup.class}, message = "Логин не должен быть пустым")
    @HasNoWhiteSpaces(groups = {CreateGroup.class, UpdateGroup.class}, message = "Логин не должен содержать пробелы")
    String login;
    String name;
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "Дата рождения не должна быть null")
    @PastOrPresent(groups = {CreateGroup.class, UpdateGroup.class}, message = "Дата рождения не может быть из будущего")
    LocalDate birthday;
    Set<Integer> friends;

    public void addFriend(int userId) {
        this.friends.add(userId);
    }

    public void deleteFriend(int userId) {
        this.friends.remove(userId);
    }
}


