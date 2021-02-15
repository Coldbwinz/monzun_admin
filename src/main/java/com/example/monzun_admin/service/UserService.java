package com.example.monzun_admin.service;

import com.example.monzun_admin.dto.UserListDTO;
import com.example.monzun_admin.entities.Mail;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.ExistsUserRequest;
import com.example.monzun_admin.request.UserRequest;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final BCryptPasswordEncoder passwordEncoder;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ModelMapper modelMapper;

    public UserService(
            BCryptPasswordEncoder passwordEncoder,
            JobService jobService,
            UserRepository userRepository,
            EmailService emailService,
            ModelMapper modelMapper
    ) {
        this.passwordEncoder = passwordEncoder;
        this.jobService  = jobService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.modelMapper = modelMapper;
    }

    /**
     * Список пользователей
     *
     * @return ListDTO
     */
    public List<UserListDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * Получение конкретного пользователя
     *
     * @param id ID пользователя
     * @return User user
     */
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    /**
     * Создание трекера
     *
     * @param request параметры трекера
     * @return User
     */
    public User create(UserRequest request) {
        User user = new User();
        String password = RandomString.make(10);

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(RoleEnum.TRACKER.getRole());
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        this.sendNewTrackerEmail(user, password);

        return user;
    }

    /**
     * Обновление пользователя
     *
     * @param id      ID пользователя
     * @param request параметры п
     * @return User
     * @throws EntityNotFoundException EntityNotFoundException
     */
    public User update(Long id, @Valid ExistsUserRequest request) throws EntityNotFoundException {
        User user = getUser(id);

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBlocked(request.isBlocked());
        user.setUpdatedAt(LocalDateTime.now());

        if (request.isBlocked()) {
            user.setBlockReason(request.getBlockReason());
        } else {
            user.setBlockReason(null);
        }

        userRepository.saveAndFlush(user);
        return user;
    }

    /**
     * Удаление пользователя
     *
     * @param id ID пользователя
     * @throws EntityNotFoundException EntityNotFoundException
     */
    public void delete(Long id) throws EntityNotFoundException {
        userRepository.delete(getUser(id));
    }

    /**
     * Изменение пароля пользователя
     *
     * @param id       ID пользователя
     * @param password пароль
     */
    public void changePassword(Long id, String password) {
        User user = getUser(id);
        user.setPassword(passwordEncoder.encode(password));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.saveAndFlush(user);
    }

    /**
     * Конвертирование Entity в DTO
     * @param user Entity
     * @return DTO
     */
    public UserListDTO convertToDto(User user) {
        return modelMapper.map(user, UserListDTO.class);
    }

    /**
     * Отправка почты новому трекеру при создании.
     *
     * @param user новый трекер
     */
    private void sendNewTrackerEmail(User user, String password) {
        StringBuilder url = new StringBuilder(ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
        url.append("");//TODO:links

        Map<String, Object> props = new LinkedHashMap<>();
        props.put("name", user.getName());
        props.put("email", user.getEmail());
        props.put("password", password);
        props.put("url", url);
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
}
