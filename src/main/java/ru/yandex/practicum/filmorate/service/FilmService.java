package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    Film addFilm(Film film);

    Film getFilm(int id);

    Film updateFilm(Film film);

    List<Film> getFilms();

    Film addLike(int filmId, int userId);

    Film deleteLike(int filmId, int userId);

    List<Film> getMostPopularFilms(int count);
}
