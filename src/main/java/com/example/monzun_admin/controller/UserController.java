package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.UserDTO;
import com.example.monzun_admin.entities.User;
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
public class UserController extends BaseRestController {

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());

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
        User updatedUser = userService.update(id, userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return userService.delete(id)
                ? ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
    }

    private UserListDTO convertToDto(User user) {
        return modelMapper.map(user, UserListDTO.class);
    }
}
