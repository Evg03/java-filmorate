package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.util.*;

@Component
@AllArgsConstructor
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbcTemplate;
    GenreStorage genreStorage;
    MpaStorage mpaStorage;

    @Override
    public Film getFilm(int id) {
        String getFilmSql = "select f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name as mpa_name " +
                "from films as f " +
                "left join mpa as m on f.mpa_id = m.id " +
                "where f.id = ?";
        String getLikesSql = "select user_id " +
                "from likes " +
                "where film_id = ?";
        String getFilmGenresSql = "select fg.genre_id, " +
                "g.name " +
                "from film_genres as fg " +
                "join genres g on fg.genre_id = g.id " +
                "where film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(getFilmSql, filmRowMapper(), id);
            List<Integer> likes = jdbcTemplate.query(getLikesSql, (rs, rowNum) -> rs.getInt("user_id"), id);
            List<Genre> genres = jdbcTemplate.query(getFilmGenresSql, (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                    rs.getString("name")), id);
            film.setLikes(new HashSet<>(likes));
            film.setGenres(new HashSet<>(genres));
            return film;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Film> getFilms() {
        String getFilmsSql = "select f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "f.mpa_id, " +
                "m.name as mpa_name " +
                "from films as f " +
                "left join mpa as m on f.mpa_id = m.id";
        String getLikesSql = "select * " +
                "from likes";
        String getGenresSql = "select fg.film_id, " +
                "fg.genre_id, " +
                "g.name " +
                "from film_genres as fg " +
                "join genres g on fg.genre_id = g.id";
        List<Film> films = jdbcTemplate.query(getFilmsSql, filmRowMapper());
        List<Map<String, Object>> likesMaps = jdbcTemplate.queryForList(getLikesSql);
        List<Map<String, Object>> genresMaps = jdbcTemplate.queryForList(getGenresSql);
        Map<Integer, Set<Integer>> likesMap = getLikesMap(likesMaps);
        Map<Integer, Set<Genre>> genresMap = getGenresMap(genresMaps);
        for (Film film : films) {
            Set<Integer> likes = likesMap.get(film.getId());
            Set<Genre> genres = genresMap.get(film.getId());
            if (likes != null) film.setLikes(likes);
            if (genres != null) film.setGenres(genres);
        }
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        String updateFilmSql = "update films set name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? where id = ?";
        String deleteGenresSql = "delete from film_genres where film_id = ?";
        String addGenresSql = "insert into film_genres (film_id, genre_id) values (?,?)";
        String deleteLikesSql = "delete from likes where film_id = ?";
        String addLikesSql = "insert into likes (film_id, user_id) values (?,?)";
        jdbcTemplate.update(updateFilmSql,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                (film.getMpa() == null) ? null : film.getMpa().getId(),
                film.getId());
        jdbcTemplate.update(deleteGenresSql, film.getId());
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(addGenresSql, film.getId(), genre.getId());
        }
        jdbcTemplate.update(deleteLikesSql, film.getId());
        for (Integer userId : film.getLikes()) {
            jdbcTemplate.update(addLikesSql, film.getId(),userId);
        }
        return getFilm(film.getId());
    }

    @Override
    public Film addFilm(Film film) {
        String insertFilmGenreSql = "insert into film_genres (film_id, genre_id) values (?,?)";
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");
        int newId = simpleJdbcInsert.executeAndReturnKey(getMap(film)).intValue();
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(insertFilmGenreSql, newId, genre.getId());
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

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            int mpaId = rs.getInt("mpa_id");
            Mpa mpa = null;
            if (mpaId != 0) {
                mpa = new Mpa(mpaId, rs.getString("mpa_name"));
            }
            Film film = Film.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .description(rs.getString("description"))
                    .releaseDate(rs.getDate("release_date").toLocalDate())
                    .duration(rs.getInt("duration"))
                    .mpa(mpa)
                    .build();
            return film;
        };
    }

    private Map<Integer, Set<Integer>> getLikesMap(List<Map<String, Object>> maps) {
        Map<Integer, Set<Integer>> likesMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            int id = (int) map.get("film_id");
            int userId = (int) map.get("user_id");
            Set<Integer> likes = likesMap.get(id);
            if (likes == null) {
                likes = new HashSet<>();
                likes.add(userId);
                likesMap.put(id, likes);
            } else {
                likes.add(userId);
            }
        }
        return likesMap;
    }

    private Map<Integer, Set<Genre>> getGenresMap(List<Map<String, Object>> maps) {
        Map<Integer, Set<Genre>> genresMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            int id = (int) map.get("film_id");
            int genreId = (int) map.get("genre_id");
            String genreName = (String) map.get("name");
            Set<Genre> genres = genresMap.get(id);
            if (genres == null) {
                genres = new HashSet<>();
                genres.add(new Genre(genreId,genreName));
                genresMap.put(id, genres);
            } else {
                genres.add(new Genre(genreId,genreName));
            }
        }
        return genresMap;
    }
}
