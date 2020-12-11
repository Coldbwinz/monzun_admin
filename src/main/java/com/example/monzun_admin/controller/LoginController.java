package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.AuthDTO;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.AuthRequest;
import com.example.monzun_admin.security.JwtUtil;
import com.example.monzun_admin.service.MyUserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Valid
@RestController
@RequestMapping("api/auth")
public class LoginController extends BaseRestController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtTokenUtil;

    public LoginController(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            MyUserDetailsService myUserDetailsService,
            JwtUtil jwtTokenUtil
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Создание JWT токена
     * @param request credentials пользователя
     * @return JSON
     */
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody AuthRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect username or password", e); log
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(this.getErrorMessage("password", "invalid password"));
        }

        /*
         * Проверка на роль авторизованного пользователя. Допускается только администратор.
         */
        User user = userRepository.findByEmail(request.getEmail());
        if (!user.getRole().equals(RoleEnum.ADMIN.getRole())) {
            return new ResponseEntity<User>(HttpStatus.FORBIDDEN);
        }

        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthDTO(jwt));
    }
}
