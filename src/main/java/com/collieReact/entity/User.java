package com.collieReact.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", updatable = false, nullable = false)
    private int userId;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "password", nullable = false, length = 25)
    private String password;

    @Column(name = "nickname", nullable = false, unique = true, length = 10)
    private String nickname;

    @Column(name = "profileImg", nullable = false, length = 255)
    private String profileImg;

    @Column(name = "createDate", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "updateDate")
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        if (this.createDate == null) {
            this.createDate = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
}
