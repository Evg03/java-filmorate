package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.group.CreateGroup;
import ru.yandex.practicum.filmorate.controller.group.UpdateGroup;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTests {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserValidationSuccessUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testUserValidationSuccessUnderUpdateGroup() {
        User user = User.builder()
                .id(4)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void notNullIdShouldBeNotValidUnderCreateGroup() {
        User user = User.builder()
                .id(1)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullIdShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void negativeIdShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(-1)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void zeroIdShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(-1)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void loginWithSpacesShouldNotBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("iva   n67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void loginWithSpacesShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(4)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("  iva    n67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyLoginShouldNotBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void emptyLoginShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(4)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullLoginShouldNotBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login(null)
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullLoginShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(4)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login(null)
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullNameShouldBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name(null)
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void nullNameShouldBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(1)
                .name(null)
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void emptyNameShouldBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void emptyNameShouldBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(1)
                .name("")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertTrue(violations.isEmpty());
    }


    @Test
    public void incorrectEmailShouldNotBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("incorrectEmail@")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void incorrectEmailShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(1)
                .name("Ivan")
                .email("incorrectEmail@")
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullEmailSouldNotBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email(null)
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullEmailSouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(1)
                .name("Ivan")
                .email(null)
                .login("ivan67")
                .birthday(LocalDate.of(2024, 1, 23))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullBirthdayShouldBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(null)
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void nullBirthdayShouldBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(1)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(null)
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void birthdayFromFutureShouldNotBeValidUnderCreateGroup() {
        User user = User.builder()
                .id(null)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, CreateGroup.class);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void birthdayFromFutureShouldNotBeValidUnderUpdateGroup() {
        User user = User.builder()
                .id(1)
                .name("Ivan")
                .email("yandex@mail.ru")
                .login("ivan67")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user, UpdateGroup.class);
        assertFalse(violations.isEmpty());
    }
}
