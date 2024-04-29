package ru.yandex.practicum.filmorate;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaDbStorage;

import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AllArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MpaDbStorageTests {

    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testGetMpaById() {
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        Mpa expected = new Mpa(1, "G");
        Mpa mpa = mpaDbStorage.getMpa(1);
        Assertions.assertThat(mpa)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    public void testGetAllMpa() {
        MpaDbStorage mpaDbStorage = new MpaDbStorage(jdbcTemplate);
        List<Mpa> expectedMpaList = new ArrayList<>();
        expectedMpaList.add(new Mpa(1,"G"));
        expectedMpaList.add(new Mpa(2,"PG"));
        expectedMpaList.add(new Mpa(3,"PG-13"));
        expectedMpaList.add(new Mpa(4,"R"));
        expectedMpaList.add(new Mpa(5,"NC-17"));
        List<Mpa> mpaList = mpaDbStorage.getAllMpa();
        Assertions.assertThat(mpaList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(expectedMpaList);
    }
}
