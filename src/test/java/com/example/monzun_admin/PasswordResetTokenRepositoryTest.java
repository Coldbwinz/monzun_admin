package com.example.monzun_admin;

import com.example.monzun_admin.entities.PasswordResetToken;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import com.example.monzun_admin.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class PasswordResetTokenRepositoryTest extends AbstractTestCase {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    public void create() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setUser(userRepository.findById(1L).isPresent() ? userRepository.findById(1L).get(): null);
        passwordResetToken.setExpiredAt(new Date());
        passwordResetToken.setToken(UUID.randomUUID().toString());
        passwordResetTokenRepository.save(passwordResetToken);
        Assertions.assertNotNull(passwordResetToken.getId());
        Assertions.assertTrue(passwordResetTokenRepository.existsById(passwordResetToken.getId()));
    }
}
