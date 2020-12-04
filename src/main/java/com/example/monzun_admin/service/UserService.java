package com.example.monzun_admin.service;

import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.model.User;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.UserRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User create(UserRequest request) {
        User user = new User();
        Date now = new Date();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(RoleEnum.TRACKER.getRole());
        user.setPassword(generatePassword());
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        userRepository.save(user);

        return user;
    }

    public User update(User user, UserRequest request) {
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBlocked(request.isBlocked());
        user.setUpdatedAt(new Date());

        if (request.isBlocked()) {
            user.setBlockReason(request.getBlockReason());
        }

        userRepository.saveAndFlush(user);
        return user;
    }

    public boolean delete(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (!user.isPresent()) {
            return false;
        }

        userRepository.delete(user.get());

        return true;
    }

    private String generatePassword() {
        return passwordEncoder.encode(RandomString.make(10));
    }
}
