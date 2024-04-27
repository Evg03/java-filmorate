package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.util.*;

@Component
@AllArgsConstructor
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUser(int id) {
        String getUserSql = "select * from users where id = ?";
        String getFriendsSql = "select subscribed_to from friends where user_id = ?";
        try {
            User user = jdbcTemplate.queryForObject(getUserSql, userRowMapper(), id);
            List<Integer> friends = jdbcTemplate.query(getFriendsSql, (rs, rowNum) -> rs.getInt("subscribed_to"), id);
            user.setFriends(new HashSet<>(friends));
            return user;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public User updateUser(User user) {
        String sql = "update users set name = ?, email = ?, login = ?, birthday = ? where id = ?";
        String deleteFriendsSql = "delete from friends where user_id = ?";
        String addFriensSql = "insert into friends (user_id, subscribed_to) values (?,?)";
        jdbcTemplate.update(sql,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        jdbcTemplate.update(deleteFriendsSql, user.getId());
        for (Integer friendId : user.getFriends()) {
            jdbcTemplate.update(addFriensSql, user.getId(), friendId);
        }
        return user;
    }

    @Override
    public User addUser(User user) {
//        String sql = "insert into users (name, email, login, birthday) values (?,?,?,?)";
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        int newId = simpleJdbcInsert.executeAndReturnKey(getMap(user)).intValue();
        user.setId(newId);
        return user;
    }

    @Override
    public List<User> getUsers() {
        String getUsersSql = "select * from users";
        String getFriendsSql = "select * from friends";
//        SqlRowSet userRowSet = jdbcTemplate.queryForRowSet(getUserSql);
        List<User> users = jdbcTemplate.query(getUsersSql, userRowMapper());
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(getFriendsSql);
        Map<Integer, Set<Integer>> friendsMap = getFriendsMap(maps);
        for (User user : users) {
            Set<Integer> friends = friendsMap.get(user.getId());
            if (friends == null) {
                user.setFriends(new HashSet<>());
            } else {
                user.setFriends(friends);
            }
        }
        return users;
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = User.builder()
                    .id(rs.getInt("id"))
                    .name(rs.getString("name"))
                    .email(rs.getString("email"))
                    .login(rs.getString("login"))
                    .birthday(rs.getDate("birthday").toLocalDate())
                    .build();
            return user;
        };
    }

    private Map<String, Object> getMap(User user) {
        Map<String, Object> values = new HashMap<>();
//        values.put("id", user.getId());
        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("birthday", Date.valueOf(user.getBirthday()));
        return values;
    }

    private Map<Integer, Set<Integer>> getFriendsMap(List<Map<String, Object>> maps) {
        Map<Integer, Set<Integer>> friendsMap = new HashMap<>();
        for (Map<String, Object> map : maps) {
            int id = (int) map.get("user_id");
            int friendId = (int) map.get("subscribed_to");
            Set<Integer> friends = friendsMap.get(id);
            if (friends == null) {
                friends = new HashSet<>();
                friends.add(friendId);
                friendsMap.put(id, friends);
            } else {
                friends.add(friendId);
            }
        }
        return friendsMap;
    }

}
