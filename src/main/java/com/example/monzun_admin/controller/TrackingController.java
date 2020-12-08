package com.example.monzun_admin.controller;

import com.example.monzun_admin.dto.TrackingDTO;
import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.exception.UserIsNotTracker;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.StartupTracking;
import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.entities.User;
import com.example.monzun_admin.repository.*;
import com.example.monzun_admin.request.TrackingRequest;
import com.example.monzun_admin.service.TrackingService;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    StartupRepository startupRepository;

    @Autowired
    private TrackingService trackingService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupTrackingRepository startupTrackingRepository;


    @GetMapping()
    public ResponseEntity<List<TrackingListDTO>> list() {
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getAllTrackings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        Optional<Tracking> possilbeTracking = trackingRepository.findById(id);
        if (!possilbeTracking.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
        }

        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(possilbeTracking.get()));
    }

    @PostMapping()
    public ResponseEntity<?> create(@Valid @RequestBody TrackingRequest trackingRequest) {
        Tracking tracking = trackingService.create(trackingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(tracking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody TrackingRequest trackingRequest) {
        Tracking updatedTracking = trackingService.update(id, trackingRequest);
        return ResponseEntity.status(HttpStatus.OK).body(trackingService.getTracking(updatedTracking));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return trackingService.delete(id)
                ? ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.getFalseResponse());
    }


    @PutMapping("/{trackingId}/startups/{startupId}/setTracker")
    public ResponseEntity<?> setStartupTracker(
            @PathVariable Long trackingId,
            @PathVariable Long startupId,
            @Valid @NotNull(message = "f") @RequestBody Long trackerId
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
        } catch (UserIsNotTracker userIsNotTracker) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(new JSONObject().put("tracker", "User is not tracker"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.getTrueResponse());
    }

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
