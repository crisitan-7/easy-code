package com.crisi.easy.repository;

import com.crisi.easy.model.User;
import com.crisi.easy.service.DbChangePublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final DbChangePublisher publisher;

    private final RowMapper<User> userMapper = (rs, rowNum) ->
            new User(rs.getLong("id"), rs.getString("name"), rs.getString("email"));

    public UserRepository(JdbcTemplate jdbcTemplate, DbChangePublisher publisher) {
        this.jdbcTemplate = jdbcTemplate;
        this.publisher = publisher;
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id IDENTITY PRIMARY KEY, name VARCHAR(100), email VARCHAR(100))");
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userMapper);
    }

    public User findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE id = ?", userMapper, id);
    }

    public int save(User user) {
        int rows = jdbcTemplate.update(
                "INSERT INTO users (name, email) VALUES (?, ?)",
                user.getName(), user.getEmail()
        );
        if (rows > 0) {
            publisher.publishChange("users", "INSERT", user);
        }
        return rows;
    }

    public int delete(Long id) {
        int rows = jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
        if (rows > 0) {
            publisher.publishChange("users", "DELETE", id);
        }
        return rows;
    }
}