package com.example.monzun_admin.controller;

import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.dto.AuthDTO;
import com.example.monzun_admin.security.JwtUtil;
import com.example.monzun_admin.request.AuthRequest;
import com.example.monzun_admin.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
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
