package com.example.monzun_admin.service;


import com.example.monzun_admin.dto.TrackingRequestListDTO;
import com.example.monzun_admin.entities.*;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.repository.TrackingRequestRepository;
import com.example.monzun_admin.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrackingRequestService {

    private final TrackingRepository trackingRepository;
    private final TrackingRequestRepository trackingRequestRepository;
    private final EmailService emailService;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final StartupTrackingRepository startupTrackingRepository;
    private final TransactionTemplate transactionTemplate;
    private final ModelMapper modelMapper;

    public TrackingRequestService(
            TrackingRepository trackingRepository,
            TrackingRequestRepository trackingRequestRepository,
            EmailService emailService,
            JobService jobService,
            UserRepository userRepository,
            StartupTrackingRepository startupTrackingRepository,
            PlatformTransactionManager transactionManager,
            ModelMapper modelMapper
    ) {
        this.trackingRepository = trackingRepository;
        this.trackingRequestRepository = trackingRequestRepository;
        this.emailService = emailService;
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.startupTrackingRepository = startupTrackingRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
        this.modelMapper = modelMapper;
    }

    public List<TrackingRequestListDTO> getRequestsByTracking(Long trackingId) throws EntityNotFoundException {
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found id = " + trackingId));

        return trackingRequestRepository.findAllByTracking(tracking)
                .stream()
                .map(this::convertToListDto)
                .collect(Collectors.toList());
    }

    public void accept(Long trackingRequestId, Long trackerId) throws EntityNotFoundException {
        TrackingRequest trackingRequest = trackingRequestRepository.findById(trackingRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking request not found id = " + trackingRequestId));

        User tracker = userRepository.findByIdAndRole(trackerId, RoleEnum.TRACKER.getRole())
                .orElseThrow(() -> new EntityNotFoundException("Tracker not found id = " + trackingRequestId));

        Tracking tracking = trackingRequest.getTracking();
        Startup startup = trackingRequest.getStartup();

        Optional<StartupTracking> optionalStartupTracking = startupTrackingRepository.findByTrackingAndStartup(tracking, startup);
        if (optionalStartupTracking.isPresent()) {
            throw new ValidationException("This startup "+ startup.getName() +" in tracking = " + tracking.getName());
        }

        StartupTracking startupTracking = new StartupTracking();
        startupTracking.setTracker(tracker);
        startupTracking.setStartup(startup);
        startupTracking.setTracking(tracking);

        transactionTemplate.executeWithoutResult(exec -> {
            startupTrackingRepository.save(startupTracking);
            trackingRequestRepository.delete(trackingRequest);
        });

        sendAcceptEmail(trackingRequest.getStartup().getOwner(), trackingRequest.getTracking());
    }

    public void decline(Long trackingRequestId) {
        TrackingRequest trackingRequest = trackingRequestRepository.findById(trackingRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking request not found id = " + trackingRequestId));

        sendDeclineEmail(trackingRequest.getStartup().getOwner(), trackingRequest.getTracking());
        trackingRequestRepository.delete(trackingRequest);
    }

    private void sendAcceptEmail(User user, Tracking tracking) {
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getName());
        props.put("tracking", tracking);
        props.put("url", "url"); //TODO:links
        Mail mail = emailService.createMail(user.getEmail(), "Welcome to tracking!", props);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("mail", mail);
        jobDataMap.put("template", "acceptTrackingRequest");
        jobDataMap.put("job-description", "Send email accepting tracking request " + user.getEmail());
        jobDataMap.put("job-group", "email");

        try {
            jobService.schedule(jobDataMap);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    private void sendDeclineEmail(User user, Tracking tracking) {
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getName());
        props.put("tracking", tracking);
        Mail mail = emailService.createMail(user.getEmail(), "Unfortunately, request is decline!", props);

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("mail", mail);
        jobDataMap.put("template", "declineTrackingRequest");
        jobDataMap.put("job-description", "Send email declining tracking request " + user.getEmail());
        jobDataMap.put("job-group", "email");

        try {
            jobService.schedule(jobDataMap);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Преобразование в DTO
     * @param trackingRequest заявка на набор
     * @return TrackingRequestDTO
     */
    private TrackingRequestListDTO convertToListDto(TrackingRequest trackingRequest) {
        return modelMapper.map(trackingRequest, TrackingRequestListDTO.class);
    }
}
