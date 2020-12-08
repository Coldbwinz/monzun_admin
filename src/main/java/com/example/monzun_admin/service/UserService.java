package com.example.monzun_admin.service;

import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.entities.Mail;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.UserRequest;
import net.bytebuddy.utility.RandomString;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JobService jobService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

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

        this.sendNewTrackerEmail(user);

        return user;
    }

    public User update(Long id, UserRequest request) {
        User user = getUser(id);

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
        userRepository.delete(getUser(id));

        return true;
    }

    public void changePassword(Long id, String password) {
        User user = getUser(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatedAt(new Date());
        userRepository.saveAndFlush(user);
    }

    private User getUser(Long id) {
        Optional<User> possibleUser = userRepository.findById(id);
        if (!possibleUser.isPresent()) {
            throw new EntityNotFoundException("User not found");
        }

        return possibleUser.get();
    }

    private void sendNewTrackerEmail(User user) {
        StringBuilder url = new StringBuilder(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        url.append("");//TODO:link

        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getName());
        props.put("button-url", url);
        Mail mail = emailService.createMail(user.getEmail(), "Welcome!", props);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("mail", mail);
        jobDataMap.put("template", "createUser");
        jobDataMap.put("job-description", "Send tracker email after creating acc " + user.getEmail());
        jobDataMap.put("job-group", "email");

        try {
            jobService.schedule(jobDataMap);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private String generatePassword() {
        return passwordEncoder.encode(RandomString.make(10));
    }
}
