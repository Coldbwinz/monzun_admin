package com.example.monzun_admin.service;


import com.example.monzun_admin.dto.TrackingDTO;
import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.entities.*;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.exception.UserIsNotTrackerException;
import com.example.monzun_admin.repository.AttachmentRepository;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.request.TrackingRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackingService {

    private final ModelMapper modelMapper;
    private final TrackingRepository trackingRepository;
    private final AttachmentRepository attachmentRepository;
    private final StartupTrackingRepository startupTrackingRepository;

    public TrackingService(
            ModelMapper modelMapper,
            TrackingRepository trackingRepository,
            AttachmentRepository attachmentRepository,
            StartupTrackingRepository startupTrackingRepository
    ) {
        this.modelMapper = modelMapper;
        this.trackingRepository = trackingRepository;
        this.attachmentRepository = attachmentRepository;
        this.startupTrackingRepository = startupTrackingRepository;
    }

    /**
     * Список наборов, преобразованный в структуру для вывода
     *
     * @return List TrackingListDTO
     */
    public List<TrackingListDTO> getAllTrackings() {
        return trackingRepository.findAll().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Конкретный трекинг, преобразованный в структуру для вывода
     *
     * @param tracking Набор
     * @return TrackingDTO
     */
    public TrackingDTO getTracking(Tracking tracking) {
        return convertToDto(tracking);
    }

    /**
     * Создание набора
     *
     * @param request характеристики набора
     * @return Tracking
     */
    public Tracking create(TrackingRequest request) {
        Tracking tracking = new Tracking();
        setRequestData(tracking, request);
        tracking.setCreatedAt(LocalDateTime.now());
        trackingRepository.save(tracking);

        return tracking;
    }

    /**
     * Редактирование набора
     *
     * @param id      ID набора
     * @param request характеристики набора
     * @return Tracking
     */
    public Tracking update(Long id, TrackingRequest request) {
        Tracking tracking = getTracking(id);
        setRequestData(tracking, request);
        tracking.setUpdatedAt(LocalDateTime.now());
        trackingRepository.save(tracking);

        return tracking;
    }

    /**
     * Удаление набора
     *
     * @param id ID набора
     * @return boolean
     */
    public boolean delete(Long id) {
        Optional<Tracking> tracking = trackingRepository.findById(id);

        if (!tracking.isPresent()) {
            return false;
        }

        trackingRepository.delete(tracking.get());

        return true;
    }

    /**
     * Добавить трекера в стартап, который в наборе.
     *
     * @param tracking набор
     * @param startup  стартап
     * @param tracker  трекер
     * @throws UserIsNotTrackerException UserIsNotTrackerException
     */
    public void addTracker(Tracking tracking, Startup startup, User tracker) throws UserIsNotTrackerException {
        if (!tracker.getRole().equals(RoleEnum.TRACKER.getRole())) {
            throw new UserIsNotTrackerException("User is not tracker");
        }

        StartupTracking startupTracking = new StartupTracking();
        startupTracking.setTracking(tracking);
        startupTracking.setStartup(startup);
        startupTracking.setTracker(tracker);
        startupTrackingRepository.save(startupTracking);
    }

    /**
     * Удалить стартап из набора
     *
     * @param tracking набор
     * @param startup  набор
     */
    public void removeStartup(Tracking tracking, Startup startup) {
        Optional<StartupTracking> startupInfo = startupTrackingRepository.findByTrackingAndStartup(tracking, startup);
        startupInfo.ifPresent(startupTrackingRepository::delete);
    }

    /**
     * Получение объекта Tracking по указанному ID
     *
     * @param id Tracking ID
     * @return Tracking
     * @throws EntityNotFoundException EntityNotFoundException
     */
    private Tracking getTracking(Long id) throws EntityNotFoundException {
        Optional<Tracking> possibleTracking = trackingRepository.findById(id);
        if (!possibleTracking.isPresent()) {
            throw new EntityNotFoundException("Tracking not found id = " + id);
        }

        return possibleTracking.get();
    }

    /**
     * Сеттер значений для Tracking Entity
     *
     * @param tracking набор
     * @param request  характеристики набора
     */
    private void setRequestData(Tracking tracking, TrackingRequest request) {
        tracking.setName(request.getName());
        tracking.setDescription(request.getDescription());
        tracking.setActive(request.isActive());
        tracking.setStartedAt(request.getStartedAt());
        tracking.setEndedAt(request.getEndedAt());

        if (request.getLogoId() != null) {
            Optional<Attachment> attachment = attachmentRepository.findById(request.getLogoId());
            attachment.ifPresent(tracking::setLogo);
        }
    }

    /**
     * Преобразование List<Entity> в ListDTO
     *
     * @param tracking Набор
     * @return ListTrackingDTO
     */
    private TrackingListDTO convertToListDto(Tracking tracking) {
        return modelMapper.map(tracking, TrackingListDTO.class);
    }

    /**
     * Преобразование Entity в DTO
     *
     * @param tracking Набор
     * @return TrackingDTO
     */
    private TrackingDTO convertToDto(Tracking tracking) {
        return modelMapper.map(tracking, TrackingDTO.class);
    }
}
