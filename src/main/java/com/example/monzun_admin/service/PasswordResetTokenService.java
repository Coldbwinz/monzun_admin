package com.example.monzun_admin.service;

import com.example.monzun_admin.model.PasswordResetToken;
import com.example.monzun_admin.model.User;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordResetTokenRepository.save(myToken);
    }

    public boolean validatePasswordResetToken(String token) {
        final PasswordResetToken passwordResetTokenToken = passwordResetTokenRepository.findByToken(token);

        return passwordResetTokenToken != null && (!isTokenExpired(passwordResetTokenToken));
    }

    private boolean isTokenExpired(PasswordResetToken token) {
        return token.getExpiredAt().before(Calendar.getInstance().getTime());
    }
}
