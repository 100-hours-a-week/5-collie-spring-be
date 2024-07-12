// PostRepository.java
package com.collieReact.repository;

import com.collieReact.entity.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {
    private final JdbcTemplate jdbcTemplate;

    public PostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Post> postRowMapper = (rs, rowNum) -> {
        Post post = new Post();
        post.setPostId(rs.getInt("post_id"));
        post.setUserId(rs.getInt("user_id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setImage(rs.getString("image"));
        post.setCreateDate(rs.getTimestamp("createDate").toLocalDateTime());
        post.setUpdateDate(rs.getTimestamp("updateDate") != null ? rs.getTimestamp("updateDate").toLocalDateTime() : null);
        post.setViewCount(rs.getInt("viewCount"));
        post.setCommentCount(rs.getInt("commentCount"));
        return post;
    };

    public List<Post> findAll() {
        String sql = "SELECT * FROM posts";
        return jdbcTemplate.query(sql, postRowMapper);
    }

    public Post findById(int postId) {
        String sql = "SELECT * FROM posts WHERE post_id = ?";
        return jdbcTemplate.queryForObject(sql, postRowMapper, postId);
    }

    public void deleteById(int postId) {
        // 관련 댓글을 먼저 삭제
        String deleteCommentsSql = "DELETE FROM comments WHERE post_id = ?";
        jdbcTemplate.update(deleteCommentsSql, postId);

        // 이제 게시물 삭제
        String deletePostSql = "DELETE FROM posts WHERE post_id = ?";
        jdbcTemplate.update(deletePostSql, postId);
    }

    public void save(Post post) {
        String sql = "INSERT INTO posts (user_id, title, content, image, createDate, viewCount, commentCount) VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, post.getUserId(), post.getTitle(), post.getContent(), post.getImage(), post.getCreateDate(), post.getViewCount(), post.getCommentCount());
    }

    public void update(Post post) {
        String sql = "UPDATE posts SET user_id = ?, title = ?, content = ?, image = ?, updateDate = ?, viewCount = ?, commentCount = ? WHERE post_id = ?";
        jdbcTemplate.update(sql, post.getUserId(), post.getTitle(), post.getContent(), post.getImage(), post.getUpdateDate(), post.getViewCount(), post.getCommentCount(), post.getPostId());
    }

    public void incrementViews(int postId) {
        String sql = "UPDATE posts SET viewCount = viewCount + 1 WHERE post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
}
