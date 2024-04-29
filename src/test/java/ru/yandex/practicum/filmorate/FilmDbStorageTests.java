package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JdbcTest
@AllArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Test
    public void testGetMostPopularFilms() {
        GenreDbStorage genreDbStorage = new GenreDbStorage(jdbcTemplate);
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate, genreDbStorage, mpaDbStorage);
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);

        User newUser1 = User.builder()
                .name("ivan")
                .login("ivan445")
                .email("testmail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        User newUser2 = User.builder()
                .name("dima")
                .login("dima149")
                .email("testmail@mail.ru")
                .birthday(LocalDate.of(1989, 1, 1))
                .build();
        User newUser3 = User.builder()
                .name("ugin")
                .login("ugin61")
                .email("testmail@mail.ru")
                .birthday(LocalDate.of(1949, 3, 1))
                .build();

        User addedUser1 = userDbStorage.addUser(newUser1);
        User addedUser2 = userDbStorage.addUser(newUser2);
        User addedUser3 = userDbStorage.addUser(newUser3);

        Film newFilm1 = Film.builder()
                .name("Зеленая миля")
                .description("Описание фильма")
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

        Film newFilm3 = Film.builder()
                .name("Лара крофт")
                .description("Расхитительница гробниц")
                .releaseDate(LocalDate.of(1987, 4, 3))
                .duration(70)
                .mpa(new Mpa(2, "PG"))
                .build();
        Film addedFilm3 = filmDbStorage.addFilm(newFilm3);
        newFilm3.setId(addedFilm3.getId());

        addedFilm1.setLikes(Set.of(addedUser1.getId(), addedUser2.getId()));
        addedFilm2.setLikes(Set.of(addedUser1.getId(), addedUser2.getId(), addedUser3.getId()));
        addedFilm3.setLikes(Set.of(addedUser3.getId()));

        filmDbStorage.updateFilm(addedFilm1);
        filmDbStorage.updateFilm(addedFilm2);
        filmDbStorage.updateFilm(addedFilm3);

        List<Film> expectedFilms = new ArrayList<>();
        expectedFilms.add(addedFilm2);
        expectedFilms.add(addedFilm1);
        expectedFilms.add(addedFilm3);

        List<Film> mostPopularFilms = filmDbStorage.getMostPopularFilms(4);

        Assertions.assertThat(mostPopularFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedFilms);
    }

}
