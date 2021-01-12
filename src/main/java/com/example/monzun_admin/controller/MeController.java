package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.UserDTO;
import com.example.monzun_admin.entities.Mail;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.exception.NoAuthUserException;
import com.example.monzun_admin.exception.UserByEmailNotFoundException;
import com.example.monzun_admin.repository.PasswordResetTokenRepository;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.PasswordChangeRequest;
import com.example.monzun_admin.service.EmailService;
import com.example.monzun_admin.service.PasswordResetTokenService;
import com.example.monzun_admin.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Контроллер для управления профилем администратора.
 */
@Validated
@RestController
@RequestMapping("/api/me")
public class MeController extends BaseRestController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    public MeController(
            UserRepository userRepository,
            UserService userService,
            EmailService emailService,
            PasswordResetTokenService passwordResetTokenService,
            PasswordResetTokenRepository passwordResetTokenRepository
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.passwordResetTokenService = passwordResetTokenService;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
    }


    /**
     * Просмотр авторизованного пользователя
     *
     * @return JSON
     */
    @ApiOperation(
            value = "Получение собственного профиля",
            notes = "Запрос информации об авторизованном пользователе. Используется для просмотра собственного профиля"
    )
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = UserDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> me() {
        try {
            return ResponseEntity.ok(new UserDTO(this.getAuthUser()));
        } catch (NoAuthUserException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Проверка токена из письма восстановления пароля. Если токен совпадает - редирект на страницу смены пароля.
     *
     * @param token    Токен из письма при запросе смены пароля
     * @param response Response
     * @throws IOException IOException
     */
    @ApiOperation(value = "Редирект на страницу изменения профиля. " +
            "Если токен совпадает - редирект на страницу смены пароля.")
    @GetMapping("/changePassword")
    public void showChangePasswordPage(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        boolean isValid = passwordResetTokenService.isValidPasswordResetToken(token);
        String redirectUrl = isValid ? "1" : "2"; //TODO: links

        response.sendRedirect(redirectUrl);
    }

    /**
     * Формирование токена для сброса пароля и отправка почты с инструкцией по смене пароля
     *
     * @param email Почта пользователя
     * @return JSON
     */
    @ApiOperation(value = "Сброс пароля", notes = "Отправка почты для сбора пароля с подтверждением в виде токена")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Пользователь не найден")
    })
    @PostMapping(value = "/resetPassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UserByEmailNotFoundException("User with email " + email + " not found");
        }
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.createPasswordResetTokenForUser(user, token);

        StringBuilder url = new StringBuilder(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        url.append("api/me/confirmReset?token=");
        url.append(token); //TODO:links

        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getName());
        props.put("url", url);

        Mail mail = emailService.createMail(user.getEmail(), "Reset password", props);

        try {
            emailService.sendEmail(mail, "resetPassword");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().body(this.getTrueResponse());
    }

    /**
     * Изменение пароля польльзователя
     *
     * @param passwordChangeRequest структура параметров при запросе
     * @return JSON
     */
    @ApiOperation(value = "Редактирование пароля",
            notes = "Изменение пароля с учетом проверки токена")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Пользователь не найден"),
            @ApiResponse(code = 422, message = "Токен для сброса пароля не валидный")
    })
    @PostMapping(value = "/savePassword", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> savePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        boolean result = passwordResetTokenService.isValidPasswordResetToken(passwordChangeRequest.getToken());

        if (!result) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        Long userId = passwordResetTokenRepository.findByToken(passwordChangeRequest.getToken()).getUser().getId();

        if (userId != null) {
            userService.changePassword(userId, passwordChangeRequest.getNewPassword());
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
