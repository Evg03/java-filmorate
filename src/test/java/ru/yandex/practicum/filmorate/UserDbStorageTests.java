package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AllArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTests {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetUserById() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUser = User.builder()
                .name("ivan")
                .login("ivan445")
                .email("testmail@mail.ru")
                .birthday(LocalDate.of(1946, 8, 20))
                .build();
        User addedUser = userDbStorage.addUser(newUser);
        newUser.setId(addedUser.getId());
        User userFromDb = userDbStorage.getUser(addedUser.getId());
        Assertions.assertThat(userFromDb)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testGetAllUsers() {
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
        List<User> newUsers = new ArrayList<>();
        newUsers.add(newUser1);
        newUsers.add(newUser2);
        User addedUser1 = userDbStorage.addUser(newUser1);
        User addedUser2 = userDbStorage.addUser(newUser2);
        newUser1.setId(addedUser1.getId());
        newUser2.setId(addedUser2.getId());
        List<User> usersFromDb = userDbStorage.getUsers();
        Assertions.assertThat(usersFromDb)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUsers);
    }

    @Test
    public void testUpdateUser() {
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        User newUser = User.builder()
                .name("semen")
                .login("semen37")
                .email("testmail@mail.ru")
                .birthday(LocalDate.of(1996, 2, 20))
                .build();
        User addedUser = userDbStorage.addUser(newUser);
        newUser.setId(addedUser.getId());
        User updatedUser = User.builder()
                .id(addedUser.getId())
                .name("updatedName")
                .login("UpdatedLogin")
                .email("updated@mail.ru")
                .birthday(LocalDate.of(1987, 4, 20))
                .build();
        userDbStorage.updateUser(updatedUser);
        User userFromDb = userDbStorage.getUser(addedUser.getId());
        Assertions.assertThat(userFromDb)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedUser);
    }
}
