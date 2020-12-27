package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.StartupListDTO;
import com.example.monzun_admin.service.StartupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("api/startups")
public class StartupController extends BaseRestController {

    private final StartupService startupService;

    public StartupController(StartupService startupService) {
        this.startupService = startupService;
    }

    /**
     * Список стартапов
     *
     * @return JSON
     */
    @GetMapping()
    public ResponseEntity<List<StartupListDTO>> list() {
        return ResponseEntity.ok().body(startupService.getStartups());
    }

    /**
     * Конкретный стартап
     *
     * @param id ID стартапа
     * @return JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(startupService.getStartup(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Удаление стартапа
     *
     * @param id ID стартапа
     * @return JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            startupService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
