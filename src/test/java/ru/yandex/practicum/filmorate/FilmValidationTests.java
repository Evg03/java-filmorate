package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FilmValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testFilmValidationSuccessUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testFilmValidationSuccessUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void notNullIdShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullIdShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeIdShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(-1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroIdShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(0)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullNameShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name(null)
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullNameShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name(null)
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyNameShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyNameShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullDescriptionShouldBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description(null)
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void nullDescriptionShouldBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description(null)
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void emptyDescriptionShouldBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void emptyDescriptionShouldBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void description200CharactersLongShouldBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("a".repeat(200))
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void description200CharactersLongShouldBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("a".repeat(200))
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void descriptionLongerThan200CharactersLongShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("a".repeat(201))
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void descriptionLongerThan200CharactersLongShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("a".repeat(201))
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullReleaseDateShouldBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(null)
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void nullReleaseDateShouldBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(null)
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void releaseDate_28_12_1895_ShouldBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(1895,12,28))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void releaseDate_28_12_1895_ShouldBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(1895,12,28))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void releaseDateBefore_28_12_1895_ShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(1895,12,27))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void releaseDateBefore_28_12_1895_ShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(1895,12,27))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void releaseDateInTheFuture_ShouldBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.now().plusDays(1))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void releaseDateInTheFutureShouldBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.now().plusDays(1))
                .duration(55)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void nullDurationShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(null)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullDurationShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(null)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeDurationShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeDurationShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroDurationShouldNotBeValidUnderCreateGroup() {
        Film film = Film.builder()
                .id(null)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(0)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroDurationShouldNotBeValidUnderUpdateGroup() {
        Film film = Film.builder()
                .id(1)
                .name("Indiana Jones")
                .description("Adventures")
                .releaseDate(LocalDate.of(2003, 1, 5))
                .duration(0)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }
}
