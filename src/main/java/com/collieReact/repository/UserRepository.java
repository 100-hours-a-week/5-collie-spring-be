// UserRepository.java
package com.collieReact.repository;

import com.collieReact.entity.User;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setNickname(rs.getString("nickname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setProfileImg(rs.getString("profileImg"));
        user.setCreateDate(rs.getTimestamp("createDate").toLocalDateTime());
        user.setUpdateDate(rs.getTimestamp("updateDate") != null ? rs.getTimestamp("updateDate").toLocalDateTime() : null);
        return user;
    };

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findById(Long userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sql, userRowMapper, userId);
    }

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, email);

        if (users.size() == 1) {
            return users.get(0);
        } else if (users.isEmpty()) {
            return null; // 또는 적절한 예외를 던질 수 있습니다.
        } else {
            throw new IncorrectResultSizeDataAccessException(1, users.size());
        }
    }

    public void deleteById(Long userId) {
        String deleteUserSql = "DELETE FROM users WHERE user_id = ?";
        String deletePostsSql = "DELETE FROM posts WHERE user_id = ?";
        jdbcTemplate.update(deleteUserSql, userId);
        jdbcTemplate.update(deletePostsSql, userId);
    }

    public void updateNickname(Long userId, String nickname) {
        String sql = "UPDATE users SET nickname = ?, updateDate = NOW() WHERE user_id = ?";
        jdbcTemplate.update(sql, nickname, userId);
    }

    public void updatePassword(Long userId, String password) {
        String sql = "UPDATE users SET password = ?, updateDate = NOW() WHERE user_id = ?";
        jdbcTemplate.update(sql, password, userId);
    }

    public void save(User user) {
        String sql = "INSERT INTO users (nickname, email, password, profileImg, createDate) VALUES (?, ?, ?, ?, NOW())";
        jdbcTemplate.update(sql, user.getNickname(), user.getEmail(), user.getPassword(), user.getProfileImg());
    }

    public void update(User user) {
        String sql = "UPDATE users SET nickname = ?, email = ?, password = ?, profileImg = ?, updateDate = NOW() WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getNickname(), user.getEmail(), user.getPassword(), user.getProfileImg(), user.getUserId());
    }

    public void updateProfilePicture(Long userId, String profilePictureUrl) {
        String sql = "UPDATE users SET profileImg = ?, updateDate = NOW() WHERE user_id = ?";
        jdbcTemplate.update(sql, profilePictureUrl, userId);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }
}
