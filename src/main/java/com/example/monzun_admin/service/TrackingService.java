package com.example.monzun_admin.service;


import com.example.monzun_admin.dto.TrackingDTO;
import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.dto.UserListDTO;
import com.example.monzun_admin.entities.*;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.exception.UserIsNotTrackerException;
import com.example.monzun_admin.repository.*;
import com.example.monzun_admin.request.ExistsTrackingRequest;
import com.example.monzun_admin.request.TrackingRequest;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
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
    private final StartupRepository startupRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public TrackingService(
            ModelMapper modelMapper,
            TrackingRepository trackingRepository,
            AttachmentRepository attachmentRepository,
            StartupTrackingRepository startupTrackingRepository,
            StartupRepository startupRepository,
            UserRepository userRepository,
            UserService userService
    ) {
        this.modelMapper = modelMapper;
        this.trackingRepository = trackingRepository;
        this.attachmentRepository = attachmentRepository;
        this.startupTrackingRepository = startupTrackingRepository;
        this.startupRepository = startupRepository;
        this.userRepository = userRepository;
        this.userService = userService;
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
     * Получение объекта Tracking по указанному ID
     *
     * @param id Tracking ID
     * @return Tracking
     * @throws EntityNotFoundException EntityNotFoundException
     */
    public Tracking getTracking(Long id) throws EntityNotFoundException {
        return trackingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found id = " + id));
    }

    /**
     * Создание набора
     *
     * @param request характеристики набора
     * @return TrackingDTO
     */
    public TrackingDTO create(TrackingRequest request) {
        Tracking tracking = new Tracking();
        setRequestData(tracking, request);
        tracking.setCreatedAt(LocalDateTime.now());
        trackingRepository.save(tracking);

        return convertToDto(tracking);
    }

    /**
     * Редактирование набора
     *
     * @param id      ID набора
     * @param request характеристики набора
     * @return Tracking
     */
    public TrackingDTO update(Long id, @Valid ExistsTrackingRequest request) throws EntityNotFoundException {
        Tracking tracking = getTracking(id);
        setRequestData(tracking, request);
        tracking.setUpdatedAt(LocalDateTime.now());
        trackingRepository.save(tracking);

        return convertToDto(tracking);
    }

    /**
     * Удаление набора
     *
     * @param id ID набора
     */
    public void delete(Long id) throws EntityNotFoundException {
        Tracking tracking = trackingRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);

        trackingRepository.delete(tracking);
    }

    /**
     * Добавить трекера в стартап, который в наборе.
     *
     * @param trackingId ID набора
     * @param startupId  ID стартапа
     * @param trackerId  ID трекера
     * @throws UserIsNotTrackerException UserIsNotTrackerException
     */
    public void addTracker(Long trackingId, Long startupId, Long trackerId)
            throws UserIsNotTrackerException, EntityNotFoundException {
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found id " + trackingId));

        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new EntityNotFoundException("Startup not found id " + startupId));

        User tracker = userRepository.findById(trackerId)
                .orElseThrow(() -> new EntityNotFoundException("Tracker in tracking not found id " + startupId));

        if (!tracker.getRole().equals(RoleEnum.TRACKER.getRole())) {
            throw new UserIsNotTrackerException(tracker);
        }

        StartupTracking startupTracking = startupTrackingRepository.findByTrackingAndStartup(tracking, startup)
                .orElseThrow(() -> new EntityNotFoundException("Startup in tracking not found"));

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
     * Список трекеров
     * @return List<UserListDTO>
     */
    public List<UserListDTO> getTrackers() {
        return userRepository.findByRole(RoleEnum.TRACKER.getRole())
                .stream()
                .map(userService::convertToDto)
                .collect(Collectors.toList());
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
