package com.crisi.easy.repository;

import com.crisi.easy.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userMapper = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userMapper);
    }

    public User findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userMapper, id);
    }

    public int save(User user) {
        return jdbcTemplate.update(
                "INSERT INTO users (name, email) VALUES (?, ?)",
                user.getName(), user.getEmail()
        );
    }
}