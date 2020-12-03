package com.example.monzun_admin.service;

import com.example.monzun_admin.exception.UserByEmailNotFound;
import com.example.monzun_admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UserByEmailNotFound {
        com.example.monzun_admin.model.User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserByEmailNotFound("User with email " + email+" not found");
        }

        return new User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}