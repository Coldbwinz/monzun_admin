package com.example.monzun_admin.controller;


import com.example.monzun_admin.service.StatisticService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@Validated
@RestController
@RequestMapping("/api/stats")
public class StatisticController extends BaseRestController {

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/{trackingId}/{startupId}")
    public ResponseEntity<?> getStats(@PathVariable Long trackingId, @PathVariable Long startupId) {
        try {
            return ResponseEntity.ok(statisticService.get(trackingId, startupId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }
}
