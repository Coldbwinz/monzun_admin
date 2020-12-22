package com.example.monzun_admin.controller;


import com.example.monzun_admin.service.WeekReportService;
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
@RequestMapping("/api/week-reports")
public class WeekReportController extends BaseRestController {

    private final WeekReportService weekReportService;

    public WeekReportController(WeekReportService weekReportService) {
        this.weekReportService = weekReportService;
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<?> show(@PathVariable Long reportId) {
        try {
            return ResponseEntity.ok(weekReportService.show(reportId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }
}
