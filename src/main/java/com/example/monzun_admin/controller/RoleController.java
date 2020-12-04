package com.example.monzun_admin.controller;

import com.example.monzun_admin.model.Role;
import com.example.monzun_admin.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/roles")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @GetMapping()
    public ResponseEntity<List<Role>> list() {
        return ResponseEntity.ok().body(roleRepository.findAll());
    }
}
