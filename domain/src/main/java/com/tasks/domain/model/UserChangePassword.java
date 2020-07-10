package com.tasks.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Table
@AllArgsConstructor
@Entity
public class UserChangePassword {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "date_changed", nullable = false)
    private LocalDateTime dateChanged;

    @Column(name = "last_password", nullable = false)
    private String lastPassword;

    public UserChangePassword(Long userId, String oldPassword) {
        this.userId = userId;
        this.lastPassword = oldPassword;
        this.dateChanged = LocalDateTime.now();
    }
}
