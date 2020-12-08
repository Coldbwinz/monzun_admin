package com.example.monzun_admin.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "password_reset_tokens", schema = "public")
public class PasswordResetToken {

    private static final int EXPIRATION = Integer.parseInt(System.getenv("TOKEN_EXPIRED_SEC"));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String token;
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "expired_at")
    private Date expiredAt = new Date(new Date().getTime() + EXPIRATION);

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
    }
}
