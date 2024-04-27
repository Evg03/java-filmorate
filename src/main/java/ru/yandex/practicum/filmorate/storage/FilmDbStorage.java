package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilm(int id) {
        Film film = jdbcTemplate.queryForObject("select f.id as film_id, f.name as film_name, f.description, f.release_date, f.duration, f.mpa_id, m.name as mpa_name from films as f inner join mpa as m on f.mpa_id = m.id where f.id = '1'", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                int filmId = rs.getInt("film_id");
                String filmName = rs.getString("film_name");
                String description = rs.getString("description");
                int duration = rs.getInt("duration");
                Mpa mpa = new Mpa();
                mpa.setId(1);
                mpa.setName("testMpa");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                Film film = Film.builder()
                        .id(filmId)
                        .name(filmName)
                        .description(description)
                        .releaseDate(releaseDate)
                        .duration(duration)
                        .likes(new HashSet<>())
//                        .mpa(mpa)
//                        .genres(new ArrayList<>())
                        .build();
                return film;
            }
        }, id);
        return film;
    }

    @Override
    public List<Film> getFilms() {
        System.out.println("test");
        int id = 1;
        Film film = jdbcTemplate.queryForObject("select f.id as film_id, f.name as film_name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name as mpa_name " +
                "from films as f " +
                "join mpa as m on f.mpa_id = m.id " +
                "where f.id = ?", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                int filmId = rs.getInt("film_id");
                String filmName = rs.getString("film_name");
                String description = rs.getString("description");
                int duration = rs.getInt("duration");
                Mpa mpa = new Mpa();
                mpa.setId(1);
                mpa.setName("testMpa");
                LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
                Film film = Film.builder()
                        .id(filmId)
                        .name(filmName)
                        .description(description)
                        .releaseDate(releaseDate)
                        .duration(duration)
                        .likes(new HashSet<>())
//                        .mpa(mpa)
//                        .genres(new ArrayList<>())
                        .build();
                return film;
            }
        }, id);
        System.out.println(film);
        return List.of(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        String insertFilmGenreSql = "insert into film_genres (film_id, genre_id) values (?,?)";
        String insertFilmLikeSql = "insert into likes (film_id, user_id) values (?,?)";
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int newId = simpleJdbcInsert.executeAndReturnKey(getMap(film)).intValue();
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(insertFilmGenreSql, newId, genre.getId());
        }
        for (Integer like : film.getLikes()) {
            jdbcTemplate.update(insertFilmLikeSql, newId, like);
        }

        film.setId(newId);
        return film;
    }

    private Map<String, Object> getMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", Date.valueOf(film.getReleaseDate()));
        values.put("duration", film.getDuration());
        if (film.getMpa() != null) {
            values.put("mpa_id", film.getMpa().getId());
        }
        return values;
    }
}
