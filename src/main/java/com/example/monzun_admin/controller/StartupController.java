package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.StartupDTO;
import com.example.monzun_admin.dto.StartupListDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.repository.StartupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/startups")
public class StartupController extends BaseRestController {

    private final StartupRepository startupRepository;
    private final ModelMapper modelMapper;

    public StartupController(StartupRepository startupRepository, ModelMapper modelMapper) {
        this.startupRepository = startupRepository;
        this.modelMapper = modelMapper;
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
        Optional<Startup> startup = startupRepository.findById(id);
        if (!startup.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());

        }
        return ResponseEntity.status(HttpStatus.OK).body(new StartupDTO(startup.get()));
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
        }
        startupRepository.delete(startup.get());

        return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
    }

    private StartupListDTO convertToDto(Startup startup) {
        return modelMapper.map(startup, StartupListDTO.class);
    }
}
