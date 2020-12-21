package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.StartupTracking;
import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.exception.UserIsNotTrackerException;
import com.example.monzun_admin.repository.StartupRepository;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.request.TrackingRequest;
import com.example.monzun_admin.service.TrackingService;
import org.springframework.http.HttpStatus;
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
    @GetMapping()
    public ResponseEntity<List<TrackingListDTO>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getAllTrackings());
    }

    /**
     * Конкретный набор
     *
     * @param id ID набора
     * @return JSON
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
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
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody TrackingRequest trackingRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.create(trackingRequest));
    }

    /**
     * Редактирование набора
     *
     * @param id              ID набора
     * @param trackingRequest параметры для редактирования набора
     * @return JSON
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TrackingRequest trackingRequest) {
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            trackingService.delete(id);
            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
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
    @PutMapping("/{trackingId}/startups/{startupId}/setTracker")
    public ResponseEntity<?> setStartupTracker(
            @PathVariable Long trackingId,
            @PathVariable Long startupId,
            @Valid @NotNull(message = "Tracker is required") @RequestBody Long trackerId
    ) throws UserIsNotTrackerException {
        trackingService.addTracker(trackingId, startupId, trackerId);
        try {
            trackingService.addTracker(trackingId, startupId, trackerId);

            return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
        } catch (UserIsNotTrackerException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(getErrorMessage("tracker", e.getMessage()));
        }
    }

    /**
     * Удаление стартапа из набора
     *
     * @param id        ID набора
     * @param startupId ID старапа
     * @return JSON
     */
    @DeleteMapping("/{id}/startups/{startupId}")
    public ResponseEntity<?> deleteStartup(@PathVariable Long id, @PathVariable Long startupId) {
        Optional<Tracking> possibleTracking = trackingRepository.findById(id);
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

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getTrueResponse());
    }
}
