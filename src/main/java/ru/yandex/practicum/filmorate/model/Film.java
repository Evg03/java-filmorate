package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.controller.validator.After;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class Film {
    @Null(groups = {CreateGroup.class}, message = "id должен быть null")
    @NotNull(groups = {UpdateGroup.class}, message = "id не должен быть null")
    @Positive(groups = UpdateGroup.class, message = "id должен быть больше 0")
    Integer id;
    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "Имя не должно быть пустым")
    String name;
    @Size(max = 200, groups = {CreateGroup.class, UpdateGroup.class}, message = "Максимальная длина описания 200 символов")
    String description;
    @After(day = 28, month = 12, year = 1895, include = true, groups = {CreateGroup.class, UpdateGroup.class},
            message = "Дата релиза должна быть не ранее 28.12.1895")
    LocalDate releaseDate;
    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "Продолжительность не должна быть null")
    @Positive(groups = {CreateGroup.class, UpdateGroup.class}, message = "Продолжительность должна быть больше 0")
    Integer duration;
}
