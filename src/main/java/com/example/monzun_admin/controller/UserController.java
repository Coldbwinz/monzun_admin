package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.UserDTO;
import com.example.monzun_admin.model.User;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.dto.UserListDTO;
import com.example.monzun_admin.request.UserRequest;
import com.example.monzun_admin.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<UserListDTO>> list() {
        List<User> users = userRepository.findAll();
        List<UserListDTO> userListDTOS = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(userListDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);

        }
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(user.get()));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody UserRequest userRequest) {
        User user = userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @PathVariable Long id, @RequestBody UserRequest userRequest) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        User updatedUSer = userService.update(user.get(), userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(updatedUSer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        return userService.delete(id)
                ? ResponseEntity.status(HttpStatus.OK).body(true)
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
    }

    private UserListDTO convertToDto(User user) {
        return modelMapper.map(user, UserListDTO.class);
    }
}
