package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.GenreNotExistException;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exception.MpaNotExistException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {

    @Autowired
    @Qualifier("filmDbStorage")
    FilmStorage filmStorage;
    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    @Autowired
    @Qualifier("mpaDbStorage")
    MpaStorage mpaStorage;

    @Autowired
    @Qualifier("genreDbStorage")
    GenreStorage genreStorage;

    @Override
    public Film addFilm(Film film) {
        Mpa mpa = film.getMpa();
        if (mpa != null) {
            int mpaId = mpa.getId();
            if (mpaStorage.getMpa(mpaId) == null) {
                log.warn("Mpa с id = {} не существует", mpaId);
                throw new MpaNotExistException(String.format("Mpa с id = %s не существует", mpaId));
            }
        }
        for (Genre genre : film.getGenres()) {
            int genreId = genre.getId();
            if (genreStorage.getGenre(genreId) == null) {
                log.warn("Жанра с id = {} не существует", genreId);
                throw new GenreNotExistException(String.format("Жанр c id = %s не существует", genreId));
            }
        }
        filmStorage.addFilm(film);
        log.info("Фильм с id = {} добавлен", film.getId());
        return film;
    }

    @Override
    public Film getFilm(int id) {
        Film film = filmStorage.getFilm(id);
        if (film == null) {
            log.warn("Фильм с id = {} не существует", id);
            throw new FilmNotFoundException(String.format("Фильм c id = %s не существует", id));
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.getFilm(film.getId()) == null) {
            log.warn("Фильм с id = {} не существует", film.getId());
            throw new FilmNotFoundException("Фильм с id = " + film.getId() + " не существует");
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
        filmStorage.updateFilm(film);
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
        filmStorage.updateFilm(film);
        return film;
    }

    @Override
    public List<Film> getMostPopularFilms(int count) {
        if (count < 1) {
            throw new IncorrectParameterException("Параметр \"count\" должен быть положительным");
        }
        List<Film> mostPopularFilms = filmStorage.getMostPopularFilms(count);
        log.info("Количество самых популярных фильмов = {}", mostPopularFilms.size());
        return mostPopularFilms;
    }
}
