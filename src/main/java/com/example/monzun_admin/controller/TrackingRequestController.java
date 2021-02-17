package com.example.monzun_admin.controller;

import com.example.monzun_admin.entities.TrackingRequest;
import com.example.monzun_admin.service.TrackingRequestService;
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
import javax.validation.ValidationException;

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
    @ApiOperation(value = "Список заявок на набор")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно", response = TrackingRequest.class),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Набор  не надйен"),
    })
    @GetMapping(value = "/{trackingId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> list(@ApiParam(required = true, value = "ID набора") @PathVariable Long trackingId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(trackingRequestService.getRequestsByTracking(trackingId));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }


    /**
     * Одобрение заявки на набор
     *
     * @param trackingRequestId ID заявки
     * @param trackerId         ID набора
     * @return JSON
     */
    @ApiOperation(value = "Одобрение заявки на набор")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Заявка не найдена"),
    })
    @PostMapping(value = "/{trackingRequestId}/{trackerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> accept(
            @ApiParam(required = true, value = "ID заявки") @PathVariable Long trackingRequestId,
            @ApiParam(required = true, value = "ID трекера") @PathVariable Long trackerId) {
        try {
            trackingRequestService.accept(trackingRequestId, trackerId);
            return ResponseEntity.status(HttpStatus.OK).body(getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(getErrorMessage("error", e.getMessage()));
        }
    }


    /**
     * Отклонение заявки на набор
     *
     * @param trackingRequestId ID заявки
     * @return JSON
     */
    @ApiOperation(value = "Отклонение заявки на набор")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Успешно"),
            @ApiResponse(code = 401, message = "Пользователь не авторизован"),
            @ApiResponse(code = 404, message = "Заявка не найдена"),
    })
    @PostMapping(value = "/{trackingRequestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> decline(
            @ApiParam(required = true, value = "ID заявки") @PathVariable Long trackingRequestId
    ) {
        try {
            trackingRequestService.decline(trackingRequestId);
            return ResponseEntity.status(HttpStatus.OK).body(getTrueResponse());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getErrorMessage("not_found", e.getMessage()));
        }
    }
}