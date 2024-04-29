package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTests {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetFilmById() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);
        Film newFilm = Film.builder()
                .name("Терминатор")
                .description("Действие происходит в далеком будущем")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(40)
                .mpa(new Mpa(1, "G"))
                .build();
        Film addedFilm = filmDbStorage.addFilm(newFilm);
        newFilm.setId(addedFilm.getId());
        Film filmFromDb = filmDbStorage.getFilm(addedFilm.getId());
        Assertions.assertThat(filmFromDb)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @SuppressWarnings("checkstyle:Regexp")
    @Test
    public void testGetAllFilms() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);
        Film newFilm1 = Film.builder()
                .name("Терминатор")
                .description("Действие происходит в далеком будущем")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(40)
                .mpa(new Mpa(1, "G"))
                .build();
        Film addedFilm1 = filmDbStorage.addFilm(newFilm1);
        newFilm1.setId(addedFilm1.getId());

        Film newFilm2 = Film.builder()
                .name("Индиана джонс")
                .description("Расхититель гробниц")
                .releaseDate(LocalDate.of(1987, 4, 3))
                .duration(50)
                .mpa(new Mpa(1, "G"))
                .build();
        Film addedFilm2 = filmDbStorage.addFilm(newFilm2);
        newFilm2.setId(addedFilm2.getId());

        List<Film> newFilms = new ArrayList<>();
        newFilms.add(newFilm1);
        newFilms.add(newFilm2);

        List<Film> filmsFromDb = filmDbStorage.getFilms();

        Assertions.assertThat(filmsFromDb)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilms);
    }

    @Test
    public void testUpdateFilm() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);
        Film newFilm = Film.builder()
                .name("Терминатор")
                .description("Действие происходит в далеком будущем")
                .releaseDate(LocalDate.of(1967, 3, 25))
                .duration(40)
                .mpa(new Mpa(1, "G"))
                .build();
        Film addedFilm = filmDbStorage.addFilm(newFilm);
        newFilm.setId(addedFilm.getId());

        Film updatedFilm = Film.builder()
                .id(addedFilm.getId())
                .name("updatedName")
                .description("updatedDescription")
                .releaseDate(LocalDate.of(1988, 5, 5))
                .duration(60)
                .mpa(new Mpa(2, "PG"))
                .build();
        filmDbStorage.updateFilm(updatedFilm);
        Film filmFromDb = filmDbStorage.getFilm(addedFilm.getId());
        Assertions.assertThat(filmFromDb)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }

}
