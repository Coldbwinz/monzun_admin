package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.TrackingDTO;
import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.StartupTracking;
import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.exception.UserIsNotTrackerException;
import com.example.monzun_admin.repository.StartupRepository;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.request.ExistsTrackingRequest;
import com.example.monzun_admin.request.TrackingRequest;
import com.example.monzun_admin.service.TrackingService;
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
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Validated
@RestController
@RequestMapping("api/trackings")
public class TrackingController extends BaseRestController {
    private final TrackingRepository trackingRepository;
    private final StartupRepository startupRepository;
    private final TrackingService trackingService;
    private final StartupTrackingRepository startupTrackingRepository;

    public TrackingController(
            TrackingRepository trackingRepository,
            StartupRepository startupRepository,
            TrackingService trackingService,
            StartupTrackingRepository startupTrackingRepository
    ) {
        this.trackingRepository = trackingRepository;
        this.startupRepository = startupRepository;
        this.trackingService = trackingService;
        this.startupTrackingRepository = startupTrackingRepository;
    }


    /**
     * Список наборов
     *
     * @return JSON
     */
    @ApiOperation(value = "Список наборов")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = TrackingListDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TrackingListDTO>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getAllTrackings());
    }


    /**
     * Конкретный набор
     *
     * @param id ID набора
     * @return JSON
     */
    @ApiOperation(value = "Просмотр конкретного набора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = TrackingDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Набор не найден"),
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> show(@ApiParam(required = true, value = "ID набора") @PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * Создание набора
     *
     * @param trackingRequest параметры набора
     * @return JSON
     */
    @ApiOperation(value = "Создание набора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = TrackingDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> create(@ApiParam @Valid @RequestBody TrackingRequest trackingRequest) {
        return ResponseEntity.ok(trackingService.create(trackingRequest));
    }


    /**
     * Редактирование набора
     *
     * @param id              ID набора
     * @param trackingRequest параметры для редактирования набора
     * @return JSON
     */
    @ApiOperation(value = "Редактирование набора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = TrackingDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Набор не найден"),
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> update(
            @ApiParam(required = true, value = "ID набора") @PathVariable Long id,
            @ApiParam(required = true) @Valid @RequestBody ExistsTrackingRequest trackingRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(trackingService.update(id, trackingRequest));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * Удаление набора
     *
     * @param id ID набора
     * @return JSON
     */
    @ApiOperation(value = "Удаление набора")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Набор не найден"),
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> delete(@ApiParam(required = true, value = "ID набора") @PathVariable Long id) {
        try {
            trackingService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Список трекеров
     *
     * @return JSON
     */
    @ApiOperation(value = "Получение списка трекеров")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
    })
    @GetMapping(value = "/tracker-list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTrackerList() {
        return ResponseEntity.ok(trackingService.getTrackers());
    }

    /**
     * Установить трекера для стартапа в наборе. Стартапы добавляются только по заявкам, сам администратор добавить
     * в набор стартап не может.
     *
     * @param trackingId ID набора
     * @param startupId  ID стартапа
     * @param trackerId  ID трекера
     * @return JSON
     */
    @ApiOperation(value = "Привязка трекера к стартапу в наборе")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Набор не найден"),
            @ApiResponse(code = 422, message = "Указанный пользователь не является трекером"),
    })
    @PutMapping(value = "/{trackingId}/startups/{startupId}/setTracker", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> setStartupTracker(
            @ApiParam(required = true, value = "ID набора") @PathVariable Long trackingId,
            @ApiParam(required = true, value = "ID стартапа") @PathVariable Long startupId,
            @ApiParam(required = true) @Valid @NotNull(message = "Tracker is required") @RequestBody Long trackerId
    ) {
        try {
            trackingService.addTracker(trackingId, startupId, trackerId);

            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (UserIsNotTrackerException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(getErrorMessage("tracker", e.getMessage()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(getErrorMessage("not_found", e.getMessage()));
        }
    }

    /**
     * Удаление стартапа из набора
     *
     * @param trackingId ID набора
     * @param startupId  ID старапа
     * @return JSON
     */
    @ApiOperation(value = "Привязка трекера к стартапу в наборе")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 403, message = "Доступ запрещен"),
            @ApiResponse(code = 404, message = "Набор или стартап не надйен"),
    })
    @DeleteMapping(value = "/{trackingId}/startups/{startupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteStartup(
            @ApiParam(required = true, value = "ID набора") @PathVariable Long trackingId,
            @ApiParam(required = true, value = "ID стартапа") @PathVariable Long startupId
    ) {
        Optional<Tracking> possibleTracking = trackingRepository.findById(trackingId);
        Optional<Startup> possibleStartup = startupRepository.findById(startupId);

        if (!possibleTracking.isPresent() || !possibleStartup.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Optional<StartupTracking> startupTracking = startupTrackingRepository.findByTrackingAndStartup(
                possibleTracking.get(),
                possibleStartup.get()
        );

        if (!startupTracking.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        trackingService.removeStartup(possibleTracking.get(), possibleStartup.get());

        return ResponseEntity.status(HttpStatus.OK).body(getTrueResponse());
    }
}
