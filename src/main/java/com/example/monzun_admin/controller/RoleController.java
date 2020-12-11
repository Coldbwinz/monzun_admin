package com.example.monzun_admin.controller;

import com.example.monzun_admin.entities.Role;
import com.example.monzun_admin.repository.RoleRepository;
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
    @GetMapping()
    public ResponseEntity<List<Role>> list() {
        return ResponseEntity.ok().body(roleRepository.findAll());
    }
}
