package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.IncorrectPathVariableException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
@AllArgsConstructor
public class FilmController {

    FilmService filmService;

    @GetMapping()
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping()
    public Film addFilm(@Validated(CreateGroup.class) @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping()
    public Film updateFilm(@Validated(UpdateGroup.class) @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        if (id < 1 || userId < 1) {
            throw new IncorrectPathVariableException("id должен быть положительным");
        }
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        if (id < 1 || userId < 0) {
            throw new IncorrectPathVariableException("id должен быть положительным");
        }
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam Optional<Integer> count) {
        if (count.isPresent()) {
            if (count.get() < 1) {
                throw new IncorrectParameterException("Параметр \"count\" должен быть положительным");
            }
            return filmService.getMostPopularFilms(count.get());
        } else {
            return filmService.getMostPopularFilms(10);
        }
    }
}
