package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.StartupDTO;
import com.example.monzun_admin.dto.StartupListDTO;
import com.example.monzun_admin.model.Startup;
import com.example.monzun_admin.repository.StartupRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/startups")
public class StartupController {

    @Autowired
    private StartupRepository startupRepository;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping()
    public ResponseEntity<List<StartupListDTO>> list() {
        List<Startup> startups = startupRepository.findAll();
        List<StartupListDTO> startupListDTOS = startups.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(startupListDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Startup> startup = startupRepository.findById(id);
        if (!startup.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);

        }
        return ResponseEntity.status(HttpStatus.OK).body(new StartupDTO(startup.get()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        Optional<Startup> startup = startupRepository.findById(id);

        if (!startup.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
        startupRepository.delete(startup.get());

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    private StartupListDTO convertToDto(Startup startup) {
        return modelMapper.map(startup, StartupListDTO.class);
    }
}
