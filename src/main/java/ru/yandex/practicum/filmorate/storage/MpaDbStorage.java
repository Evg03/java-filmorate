package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Component
@Qualifier("mpaDbStorage")
@AllArgsConstructor
public class MpaDbStorage implements MpaStorage {

    JdbcTemplate jdbcTemplate;

    @Override
    public Mpa getMpa(int mpaid) {
        String sql = "select * from mpa where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, mpaRowMapper(), mpaid);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "select * from mpa";
        return jdbcTemplate.query(sql, mpaRowMapper());
    }

    private RowMapper<Mpa> mpaRowMapper() {
        return (rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name"));
    }
}
