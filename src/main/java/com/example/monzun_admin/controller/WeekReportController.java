package com.example.monzun_admin.controller;


import com.example.monzun_admin.dto.WeekReportDTO;
import com.example.monzun_admin.service.WeekReportService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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


    /**
     * Просмотр отчета трекера
     *
     * @param reportId ID отчета
     * @return JSON
     */
    @ApiOperation(value = "Просмотр отчета трекера")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = WeekReportDTO.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Отчет не найден"),
    })
    @GetMapping(value = "/{reportId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> show(@ApiParam(required = true, value = "ID отчета") @PathVariable Long reportId) {
        try {
            return ResponseEntity.ok(weekReportService.show(reportId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }
}
