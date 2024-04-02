package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    FilmStorage filmStorage;
    UserStorage userStorage;

    @Override
    public Film addFilm(Film film) {
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        filmStorage.addFilm(film);
        log.info("Фильм с id = {} добавлен", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.getFilm(film.getId()) == null) {
            log.warn("Фильм с id = {} не существует", film.getId());
            throw new FilmNotFoundException("Фильм с id = " + film.getId() + " не существует");
        }
        if (film.getLikes() == null) {
            film.setLikes(new HashSet<>());
        }
        filmStorage.updateFilm(film);
        log.info("Фильм с id = {} обновлён", film.getId());
        return film;
    }

    @Override
    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        log.info("Количество фильмов = {}", films.size());
        return filmStorage.getFilms();
    }


    @Override
    public Film addLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            log.warn("Фильм с id = {} не существует", filmId);
            throw new FilmNotFoundException("Фильм с id = " + filmId + " не существует");
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            log.warn("Пользователь с id = {} не существует", filmId);
            throw new FilmNotFoundException("Пользователь с id = " + filmId + " не существует");
        }
        film.addLike(userId);
        return film;
    }

    @Override
    public Film deleteLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            log.warn("Фильм с id = {} не существует", filmId);
            throw new FilmNotFoundException("Фильм с id = " + filmId + " не существует");
        }
        User user = userStorage.getUser(userId);
        if (user == null) {
            log.warn("Пользователь с id = {} не существует", filmId);
            throw new FilmNotFoundException("Пользователь с id = " + filmId + " не существует");
        }
        film.deleteLike(userId);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        if (count < 1) {
            throw new IncorrectParameterException("Параметр \"count\" должен быть положительным");
        }
        List<Film> mostPopularFilms = filmStorage.getFilms().stream()
                .sorted(Comparator.comparingInt(o -> -((Film) o).getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        log.info("Количество самых популярных фильмов = {}", mostPopularFilms.size());
        return mostPopularFilms;
    }
}
