package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.StartupListDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.repository.StartupRepository;
import com.example.monzun_admin.service.StartupService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/startups")
public class StartupController extends BaseRestController {

    private final StartupRepository startupRepository;
    private final StartupService startupService;
    private final ModelMapper modelMapper;

    public StartupController(
            StartupRepository startupRepository,
            ModelMapper modelMapper,
            StartupService startupService
    ) {
        this.startupRepository = startupRepository;
        this.modelMapper = modelMapper;
        this.startupService = startupService;
    }

    /**
     * Список стартапов
     *
     * @return JSON
     */
    @GetMapping()
    public ResponseEntity<List<StartupListDTO>> list() {
        List<Startup> startups = startupRepository.findAll();
        List<StartupListDTO> startupListDTOS = startups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(startupListDTOS);
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
        Optional<Startup> startup = startupRepository.findById(id);

        if (!startup.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        startupRepository.delete(startup.get());

        return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
    }

    private StartupListDTO convertToDto(Startup startup) {
        return modelMapper.map(startup, StartupListDTO.class);
    }
}
