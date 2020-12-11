package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.UserDTO;
import com.example.monzun_admin.dto.UserListDTO;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.UserRequest;
import com.example.monzun_admin.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("api/users")
public class UserController extends BaseRestController {

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Список пользователей
     *
     * @return JSON
     */
    @GetMapping()
    public ResponseEntity<List<UserListDTO>> list() {
        return ResponseEntity.ok().body(this.userService.getAllUsers());
    }

    /**
     * Конкретный пользователь
     *
     * @param id ID пользователя
     * @return JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> possibleUser = userRepository.findById(id);
        if (!possibleUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());

        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(possibleUser.get()));
    }

    /**
     * Создание трекера. В админке допускается только создание трекеров
     *
     * @param userRequest параметры трекера
     * @return JSON
     */
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
        User user = userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(user));
    }

    /**
     * Редактирование пользователя
     *
     * @param id          ID пользователя
     * @param userRequest параметры трекера
     * @return JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody UserRequest userRequest) {
        User updatedUser = userService.update(id, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(updatedUser));
    }

    /**
     * Удаление пользователя
     *
     * @param id ID пользователя
     * @return JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return userService.delete(id)
                ? ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
    }
}
