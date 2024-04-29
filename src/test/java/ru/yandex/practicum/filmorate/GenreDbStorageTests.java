package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTests {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetGenreById() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        Genre expected = new Genre(1, "Комедия");
        Genre genre = genreDbStorage.getGenre(1);
        Assertions.assertThat(genre)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testGetAllMpa() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        List<Genre> expectedGenresList = new ArrayList<>();
        expectedGenresList.add(new Genre(1, "Комедия"));
        expectedGenresList.add(new Genre(2, "Драма"));
        expectedGenresList.add(new Genre(3, "Мультфильм"));
        expectedGenresList.add(new Genre(4, "Триллер"));
        expectedGenresList.add(new Genre(5, "Документальный"));
        expectedGenresList.add(new Genre(6, "Боевик"));
        List<Genre> genresList = genreDbStorage.getAllGenres();
        Assertions.assertThat(genresList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedGenresList);
    }
}
