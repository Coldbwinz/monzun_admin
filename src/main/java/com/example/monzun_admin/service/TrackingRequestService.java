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
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found id = " + trackingId));

        return trackingRequestRepository.findAllByTracking(tracking);
    }

    public void accept(Long trackingRequestId, Long trackerId) throws EntityNotFoundException {
        TrackingRequest trackingRequest = trackingRequestRepository.findById(trackingRequestId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking request not found id = " + trackingRequestId));

        User tracker = userRepository.findByIdAndRole(trackerId, RoleEnum.TRACKER.getRole())
                .orElseThrow(() -> new EntityNotFoundException("Tracker not found id = " + trackingRequestId));

        StartupTracking startupTracking = new StartupTracking();
        startupTracking.setTracker(tracker);
        startupTracking.setStartup(trackingRequest.getStartup());
        startupTracking.setTracking(trackingRequest.getTracking());

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
}
