package com.example.monzun_admin.service;

import com.example.monzun_admin.entities.PasswordResetToken;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken();
        myToken.setUser(user);
        myToken.setToken(token);
        passwordResetTokenRepository.save(myToken);
    }

    public boolean validatePasswordResetToken(String token) {
        final PasswordResetToken passwordResetTokenToken = passwordResetTokenRepository.findByToken(token);

        return passwordResetTokenToken != null && (!isTokenExpired(passwordResetTokenToken));
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiredAt().isBefore(LocalDateTime.now());
    }
}
