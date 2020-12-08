package com.example.monzun_admin.service;


import com.example.monzun_admin.dto.AttachmentShortDTO;
import com.example.monzun_admin.dto.TrackingDTO;
import com.example.monzun_admin.dto.TrackingListDTO;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.exception.UserIsNotTracker;
import com.example.monzun_admin.entities.*;
import com.example.monzun_admin.repository.*;
import com.example.monzun_admin.request.TrackingRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackingService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TrackingRepository trackingRepository;

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private StartupTrackingRepository startupTrackingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StartupRepository startupRepository;


    public List<TrackingListDTO> getAllTrackings() {
        return trackingRepository.findAll().stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public TrackingDTO getTracking(Tracking tracking) {
        return convertToDto(tracking);
    }

    public Tracking create(TrackingRequest request) {
        Tracking tracking = new Tracking();
        setRequestData(tracking, request);
        tracking.setCreatedAt(new Date());
        trackingRepository.save(tracking);

        return tracking;
    }

    public Tracking update(Long id, TrackingRequest request) {
        Tracking tracking = getTracking(id);
        setRequestData(tracking, request);
        tracking.setUpdatedAt(new Date());
        trackingRepository.save(tracking);

        return tracking;
    }

    public boolean delete(Long id) {
        Optional<Tracking> tracking = trackingRepository.findById(id);

        if (!tracking.isPresent()) {
            return false;
        }

        trackingRepository.delete(tracking.get());

        return true;
    }


    public void addTracker(Tracking tracking, Startup startup, User tracker) throws UserIsNotTracker {
        if (!tracker.getRole().equals(RoleEnum.TRACKER.getRole())) {
            throw new UserIsNotTracker("User is not tracker");
        }

        StartupTracking startupTracking = new StartupTracking();
        startupTracking.setTracking(tracking);
        startupTracking.setStartup(startup);
        startupTracking.setTracker(tracker);
        startupTrackingRepository.save(startupTracking);
    }

    public void removeStartup(Tracking tracking, Startup startup) {
        Optional<StartupTracking> startupInfo =  startupTrackingRepository.findByTrackingAndStartup(tracking, startup);
        startupInfo.ifPresent(startupTracking -> startupTrackingRepository.delete(startupTracking));
    }

    private Tracking getTracking(Long id) throws EntityNotFoundException {
        Optional<Tracking> possibleTracking = trackingRepository.findById(id);
        if (!possibleTracking.isPresent()) {
            throw new EntityNotFoundException("Tracking not found id = " + id);
        }

        return possibleTracking.get();
    }

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

    private TrackingListDTO convertToListDto(Tracking tracking) {
        return modelMapper.map(tracking, TrackingListDTO.class);
    }

    private TrackingDTO convertToDto(Tracking tracking) {
        return modelMapper.map(tracking, TrackingDTO.class);
    }
}
