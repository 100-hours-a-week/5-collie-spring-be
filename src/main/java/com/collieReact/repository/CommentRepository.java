package com.collieReact.repository;

import com.collieReact.entity.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public class CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Comment> commentRowMapper = (rs, rowNum) -> {
        Comment comment = new Comment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setPostId(rs.getInt("post_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setComment(rs.getString("comment"));
        comment.setCreateDate(rs.getTimestamp("createDate").toLocalDateTime());
        comment.setUpdateDate(rs.getTimestamp("updateDate") != null ? rs.getTimestamp("updateDate").toLocalDateTime() : null);
        return comment;
    };

    public List<Comment> findByPostId(int postId) {
        String sql = "SELECT c.comment_id, c.post_id, c.comment, c.user_id, c.createDate, c.updateDate, u.nickname, u.profileImg " +
                "FROM comments c " +
                "LEFT JOIN users u ON c.user_id = u.user_id " +
                "WHERE c.post_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper, postId);
    }

    public Comment findById(int commentId) {
        String sql = "SELECT * FROM comments WHERE comment_id = ?";
        return jdbcTemplate.queryForObject(sql, commentRowMapper, commentId);
    }

    public void deleteById(int commentId) {
        String sql = "DELETE FROM comments WHERE comment_id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    public void save(Comment comment) {
        String sql = "INSERT INTO comments (post_id, user_id, comment, createDate) VALUES (?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(sql, comment.getPostId(), comment.getUserId(), comment.getComment(), now);
    }

    public void update(Comment comment) {
        String sql = "UPDATE comments SET comment = ?, updateDate = ? WHERE comment_id = ?";
        jdbcTemplate.update(sql, comment.getComment(), comment.getUpdateDate(), comment.getCommentId());
    }
}
