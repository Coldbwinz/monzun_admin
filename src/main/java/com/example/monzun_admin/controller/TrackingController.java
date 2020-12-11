package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.StartupTracking;
import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.exception.UserIsNotTrackerException;
import com.example.monzun_admin.repository.StartupRepository;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.repository.UserRepository;
import com.example.monzun_admin.request.TrackingRequest;
import com.example.monzun_admin.service.TrackingService;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private final UserRepository userRepository;
    private final StartupTrackingRepository startupTrackingRepository;

    public TrackingController(
            TrackingRepository trackingRepository,
            StartupRepository startupRepository,
            TrackingService trackingService,
            UserRepository userRepository,
            StartupTrackingRepository startupTrackingRepository
    ) {
        this.trackingRepository = trackingRepository;
        this.startupRepository = startupRepository;
        this.trackingService = trackingService;
        this.userRepository = userRepository;
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
        Optional<Tracking> possilbeTracking = trackingRepository.findById(id);
        if (!possilbeTracking.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
        }

        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(possilbeTracking.get()));
    }

    /**
     * Создание набора
     *
     * @param trackingRequest параметры набора
     * @return JSON
     */
    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody TrackingRequest trackingRequest) {
        Tracking tracking = trackingService.create(trackingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(tracking));
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
        Tracking updatedTracking = trackingService.update(id, trackingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(updatedTracking));
    }

    /**
     * Удаление набора
     *
     * @param id ID набора
     * @return JSON
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return trackingService.delete(id)
                ? ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
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
    ) {
        Optional<Tracking> possibleTracking = trackingRepository.findById(trackingId);
        Optional<Startup> possibleStartup = startupRepository.findById(startupId);
        Optional<User> possibleTracker = userRepository.findById(trackerId);

        if (!possibleStartup.isPresent() || !possibleTracking.isPresent() || !possibleTracker.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
        }

        Optional<StartupTracking> startupTracking = startupTrackingRepository.findByTrackingAndStartup(
                possibleTracking.get(),
                possibleStartup.get()
        );

        if (!startupTracking.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(this.getFalseResponse());
        }

        try {
            trackingService.addTracker(possibleTracking.get(), possibleStartup.get(), possibleTracker.get());
        } catch (UserIsNotTrackerException userIsNotTrackerException) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new JSONObject().put("tracker", "User is not tracker"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
        }

        Optional<StartupTracking> startupTracking = startupTrackingRepository.findByTrackingAndStartup(
                possibleTracking.get(),
                possibleStartup.get()
        );

        if (!startupTracking.isPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(this.getFalseResponse());
        }

        trackingService.removeStartup(possibleTracking.get(), possibleStartup.get());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getTrueResponse());
    }
}
