package com.example.monzun_admin.service;


import com.example.monzun_admin.entities.*;
import com.example.monzun_admin.enums.RoleEnum;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.repository.TrackingRequestRepository;
import com.example.monzun_admin.repository.UserRepository;
import org.quartz.JobDataMap;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TrackingRequestService {

    private final TrackingRepository trackingRepository;
    private final TrackingRequestRepository trackingRequestRepository;
    private final EmailService emailService;
    private final JobService jobService;
    private final UserRepository userRepository;
    private final StartupTrackingRepository startupTrackingRepository;
    private final TransactionTemplate transactionTemplate;

    public TrackingRequestService(
            TrackingRepository trackingRepository,
            TrackingRequestRepository trackingRequestRepository,
            EmailService emailService,
            JobService jobService,
            UserRepository userRepository,
            StartupTrackingRepository startupTrackingRepository,
            PlatformTransactionManager transactionManager
    ) {
        this.trackingRepository = trackingRepository;
        this.trackingRequestRepository = trackingRequestRepository;
        this.emailService = emailService;
        this.jobService = jobService;
        this.userRepository = userRepository;
        this.startupTrackingRepository = startupTrackingRepository;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    public List<TrackingRequest> getRequestsByTracking(Long trackingId) throws EntityNotFoundException {
        Optional<Tracking> possibleTracking = trackingRepository.findById(trackingId);
        if (!possibleTracking.isPresent()) {
            throw new EntityNotFoundException("Tracking not found id = " + trackingId);
        }

        return trackingRequestRepository.findAllByTracking(possibleTracking.get());
    }

    public void accept(Long trackingRequestId, Long trackerId) {
        Optional<TrackingRequest> possibleTrackingRequest = trackingRequestRepository.findById(trackingRequestId);
        if (!possibleTrackingRequest.isPresent()) {
            throw new EntityNotFoundException("Tracking request not found id = " + trackingRequestId);
        }

        Optional<User> possibleTracker = userRepository.findByIdAndRole(trackerId, RoleEnum.TRACKER.getRole());
        if (!possibleTracker.isPresent()) {
            throw new EntityNotFoundException("Tracker not found id = " + trackingRequestId);
        }

        TrackingRequest trackingRequest = possibleTrackingRequest.get();
        StartupTracking startupTracking = new StartupTracking();
        startupTracking.setTracker(possibleTracker.get());
        startupTracking.setStartup(trackingRequest.getStartup());
        startupTracking.setTracking(trackingRequest.getTracking());

        transactionTemplate.executeWithoutResult(exec -> {
            startupTrackingRepository.save(startupTracking);
            trackingRequestRepository.delete(trackingRequest);
        });

        sendAcceptEmail(trackingRequest.getStartup().getOwner(), trackingRequest.getTracking());
    }

    public void decline(Long trackingRequestId) {
        Optional<TrackingRequest> possibleTrackingRequest = trackingRequestRepository.findById(trackingRequestId);
        if (!possibleTrackingRequest.isPresent()) {
            throw new EntityNotFoundException("Tracking request not found id = " + trackingRequestId);
        }

        TrackingRequest trackingRequest = possibleTrackingRequest.get();
        sendDeclineEmail(trackingRequest.getStartup().getOwner(), trackingRequest.getTracking());
        trackingRequestRepository.delete(possibleTrackingRequest.get());
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
}
