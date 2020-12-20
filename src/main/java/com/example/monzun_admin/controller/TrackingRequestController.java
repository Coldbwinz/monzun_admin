package com.example.monzun_admin.controller;

import com.example.monzun_admin.service.TrackingRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@Validated
@RestController
@RequestMapping("api/requests")
public class TrackingRequestController extends BaseRestController {

    private final TrackingRequestService trackingRequestService;

    public TrackingRequestController(TrackingRequestService trackingRequestService) {
        this.trackingRequestService = trackingRequestService;
    }

    /**
     * Список заявок на набор
     *
     * @return JSON
     */
    @GetMapping("/{trackingId}")
    public ResponseEntity<?> list(@PathVariable Long trackingId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(trackingRequestService.getRequestsByTracking(trackingId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }

    @PostMapping("/{trackingRequestId}/{trackerId}")
    public ResponseEntity<?> accept(@PathVariable Long trackingRequestId, @PathVariable Long trackerId) {
        try {
            trackingRequestService.accept(trackingRequestId, trackerId);
            return ResponseEntity.status(HttpStatus.OK).body(getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }

    @PostMapping("/{trackingRequestId}")
    public ResponseEntity<?> decline(@PathVariable Long trackingRequestId) {
        try {
            trackingRequestService.decline(trackingRequestId);
            return ResponseEntity.status(HttpStatus.OK).body(getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }
}