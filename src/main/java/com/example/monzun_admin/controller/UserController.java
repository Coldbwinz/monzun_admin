package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.UserDTO;
import com.example.monzun_admin.dto.UserListDTO;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.request.UserRequest;
import com.example.monzun_admin.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/users")
public class UserController extends BaseRestController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Список пользователей
     *
     * @return JSON
     */
    @ApiOperation(value = "Список пользователей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = UserListDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserListDTO>> list() {
        return ResponseEntity.ok().body(this.userService.getAllUsers());
    }


    /**
     * Конкретный пользователь
     *
     * @param id ID пользователя
     * @return JSON
     */
    @ApiOperation(value = "Просмотр контретного пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = UserListDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Пользователь не найден"),
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> show(@ApiParam(required = true, value = "ID пользователя") @PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(this.userService.getUser(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }


    /**
     * Создание трекера. В админке допускается только создание трекеров
     *
     * @param userRequest параметры трекера
     * @return JSON
     */
    @ApiOperation(value = "Добавление пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = UserDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@ApiParam @Valid @RequestBody UserRequest userRequest) {
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
    @ApiOperation(value = "Редактирование пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = UserListDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Пользователь не найден"),
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @ApiParam(required = true, value = "ID пользователя") @Valid @PathVariable Long id,
            @ApiParam @RequestBody UserRequest userRequest) {
        try {
            User updatedUser = userService.update(id, userRequest);
            return ResponseEntity.status(HttpStatus.OK).body(new UserDTO(updatedUser));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }


    /**
     * Удаление пользователя
     *
     * @param id ID пользователя
     * @return JSON
     */
    @ApiOperation(value = "Удаление пользователя")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = UserListDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Пользователь не найден"),
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@ApiParam(required = true, value = "ID пользователя") @Valid @PathVariable Long id) {
        try {
            userService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }
}
