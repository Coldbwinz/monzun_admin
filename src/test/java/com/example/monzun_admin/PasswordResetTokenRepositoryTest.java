package com.example.monzun_admin;

import com.example.monzun_admin.entities.PasswordResetToken;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import com.example.monzun_admin.repository.UserRepository;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@AutoConfigureTestDatabase(replace = NONE)
@RunWith(DataProviderRunner.class)
@ContextConfiguration
@DataJpaTest
public class PasswordResetTokenRepositoryTest extends AbstractTestCase {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void create() {
        User user = createTestUser();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiredAt(LocalDateTime.now());
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetTokenRepository.saveAndFlush(passwordResetToken);
        Assertions.assertNotNull(passwordResetToken.getId());
        Assertions.assertTrue(passwordResetTokenRepository.existsById(passwordResetToken.getId()));
    }

    private User createTestUser() {
        User user = new User();
        user.setName("tester");
        user.setEmail("test@mail.ru");
        user.setPassword("test");
        userRepository.save(user);
        return user;
    }
}