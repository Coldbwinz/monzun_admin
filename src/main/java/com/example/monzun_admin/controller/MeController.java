package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.PasswordChangeDTO;
import com.example.monzun_admin.exception.UserByEmailNotFound;
import com.example.monzun_admin.model.Mail;
import com.example.monzun_admin.model.User;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.service.EmailService;
import com.example.monzun_admin.service.PasswordResetTokenService;
import com.example.monzun_admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/me")
public class MeController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @GetMapping("/user/changePassword")
    public void showChangePasswordPage(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        boolean isValid = passwordResetTokenService.validatePasswordResetToken(token);
        String redirectUrl = isValid ? "1" : "2"; //TODO: need links

        response.sendRedirect(redirectUrl);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Boolean> resetPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFound("User with email " + email + " not found");
        }
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user, token);

        Mail mail = new Mail();
        mail.setFrom(environment.getProperty("SYSTEM_EMAIL_FROM"));
        mail.setMailTo(user.getEmail());
        mail.setSubject("Reset password");

        StringBuilder url = new StringBuilder(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        url.append("api/me/confirmReset?token=");
        url.append(token);

        Map<String, Object> model = new HashMap<>();
        model.put("name", user.getName());
        model.put("button-url", url);
        model.put("sign", environment.getProperty("APP_NAME"));
        mail.setProps(model);

        try {
            emailService.sendEmail(mail, "resetPassword");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok(true);
    }

    @PostMapping("/user/savePassword")
    public ResponseEntity<Boolean> savePassword(@Valid PasswordChangeDTO passwordChangeDTO) {
        boolean result = passwordResetTokenService.validatePasswordResetToken(passwordChangeDTO.getToken());

        if (!result) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(false);
        }

        User user = passwordResetTokenRepository.findByToken(passwordChangeDTO.getToken()).getUser();

        if (user != null) {
            userService.changePassword(user, passwordChangeDTO.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).body(false);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
