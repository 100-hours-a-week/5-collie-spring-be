package com.collieReact.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", nullable = false, updatable = false)
    private int postId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "title", nullable = false, length = 25)
    private String title;

    @Lob
    @Column(name = "content", nullable = false, columnDefinition="TEXT")
    private String content;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "createDate", nullable = false, updatable = false)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime createDate;

    @Column(name = "updateDate")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime updateDate;

    @Column(name = "viewCount", nullable = false)
    private int viewCount = 0;

    @Column(name = "commentCount", nullable = true)
    private int commentCount = 0;

    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
        if (this.viewCount == 0) {
            this.viewCount = 0;
        }
        if (this.commentCount == 0) {
            this.commentCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
