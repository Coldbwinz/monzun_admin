package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.StartupDTO;
import com.example.monzun_admin.dto.StartupListDTO;
import com.example.monzun_admin.service.StartupService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    @ApiOperation(value = "Список стартапов")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = StartupListDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не найден")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StartupListDTO>> list() {
        return ResponseEntity.ok().body(startupService.getStartups());
    }


    /**
     * Конкретный стартап
     *
     * @param id ID стартапа
     * @return JSON
     */
    @ApiOperation(value = "Просмотр конкретного стартапа")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = StartupDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не найден"),
            @ApiResponse(code = 404, message = "Стартап не найден"),
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> show(
            @ApiParam(required = true, value = "ID стартапа") @PathVariable Long id) {
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
    @ApiOperation(value = "Удаление конкретного стартапа")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не найден"),
            @ApiResponse(code = 404, message = "Стартап не найден"),
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@ApiParam(required = true, value = "ID стартапа") @PathVariable Long id) {
        try {
            startupService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
