package ru.yandex.practicum.filmorate.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    GenreStorage genreStorage;

    @Override
    public Genre getGenre(int genreId) {
        Genre genre = genreStorage.getGenre(genreId);
        if (genre == null) {
            log.warn("Жанра с id = {} не существует", genreId);
            throw new GenreNotFoundException(String.format("Жанр c id = %s не существует", genreId));
        }
        return genre;
    }

    @Override
    public List<Genre> getAllGenres() {
        List<Genre> genres = genreStorage.getAllGenres();
        log.info("Количество жанров = {}", genres.size());
        return genres;
    }
}
