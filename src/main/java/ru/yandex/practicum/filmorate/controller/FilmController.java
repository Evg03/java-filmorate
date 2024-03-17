package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    Map<Integer, Film> films = new HashMap<>();
    int idCounter = 1;

    @GetMapping()
    public List<Film> getFilms() {
        log.info("Количество фильмов = " + films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping()
    public Film addFilm(@Validated(CreateGroup.class) @RequestBody Film film) {
        Film newFilm = film.toBuilder().id(idCounter++).build();
        films.put(newFilm.getId(), newFilm);
        log.info("Фильм с id = " + newFilm.getId() + " добавлен");
        return newFilm;
    }

    @PutMapping()
    public Film updateFilm(@Validated(UpdateGroup.class) @RequestBody Film film) {
        if (films.get(film.getId()) == null) {
            log.warn("Фильм с id = " + film.getId() + " не существует");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Фильм с id = " + film.getId() + " не существует");
        }
        films.put(film.getId(), film);
        log.info("Фильм с id = " + film.getId() + " обновлён");
        return film;
    }
}
