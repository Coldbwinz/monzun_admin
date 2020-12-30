package com.example.monzun_admin.controller;

import com.example.monzun_admin.entities.Role;
import com.example.monzun_admin.repository.RoleRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Список ролей
     * @return JSON
     */
    @ApiOperation(value = "Список ролей")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = Role.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Role>> list() {
        return ResponseEntity.ok().body(roleRepository.findAll());
    }
}
