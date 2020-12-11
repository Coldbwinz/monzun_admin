package com.example.monzun_admin.service;

import com.example.monzun_admin.entities.PasswordResetToken;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public PasswordResetTokenService(PasswordResetTokenRepository passwordResetTokenRepository) {
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }

    /**
     * Создание токена для восстановления пароля
     *
     * @param user  Пользователь
     * @param token токен
     */
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        passwordResetTokenRepository.save(myToken);
    }

    /**
     * Валидация токена восстановления пароля
     *
     * @param token токен восстановления пароля
     * @return boolean
     */
    public boolean isValidPasswordResetToken(String token) {
        final PasswordResetToken passwordResetTokenToken = passwordResetTokenRepository.findByToken(token);

        return passwordResetTokenToken != null && (!isTokenExpired(passwordResetTokenToken));
    }

    /**
     * Проверка времени жизни токена
     *
     * @param token токен восстановления пароля
     * @return boolean
     */
    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiredAt().isBefore(LocalDateTime.now());
    }
}
