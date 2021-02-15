package com.example.monzun_admin.service;

import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.StartupTracking;
import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.entities.WeekReport;
import com.example.monzun_admin.repository.StartupRepository;
import com.example.monzun_admin.repository.StartupTrackingRepository;
import com.example.monzun_admin.repository.TrackingRepository;
import com.example.monzun_admin.repository.WeekReportRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;


@Service
public class StatisticService {

    private final WeekReportRepository weekReportRepository;
    private final TrackingRepository trackingRepository;
    private final StartupRepository startupRepository;
    private final StartupTrackingRepository startupTrackingRepository;

    public StatisticService(
            WeekReportRepository weekReportRepository,
            TrackingRepository trackingRepository,
            StartupRepository startupRepository,
            StartupTrackingRepository startupTrackingRepository
    ) {
        this.weekReportRepository = weekReportRepository;
        this.trackingRepository = trackingRepository;
        this.startupRepository = startupRepository;
        this.startupTrackingRepository = startupTrackingRepository;
    }

    /**
     * Получение статистики по неделям набора. В статистику входят отчеты трекеров.
     *
     * @param trackingId ID набора
     * @param startupId  ID стартапа
     * @return Map
     * @throws EntityNotFoundException EntityNotFoundException
     */
    public Map<String, Object> get(Long trackingId, Long startupId)
            throws EntityNotFoundException, AccessDeniedException {
        Tracking tracking = trackingRepository.findById(trackingId)
                .orElseThrow(() -> new EntityNotFoundException("Tracking not found id " + trackingId));

        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new EntityNotFoundException("Startup not found id " + startupId));

        StartupTracking startupTracking = startupTrackingRepository.findByTrackingAndStartup(tracking, startup)
                .orElseThrow(() -> new EntityNotFoundException("Startup id " + startupId
                        + "not found in tracking id " + trackingId));

        List<WeekReport> weekReportList = weekReportRepository.findWeekReportsByTrackingAndStartup(tracking, startup);

        Map<String, Object> statistic = new HashMap<>();
        Float avgEstimate = (float) weekReportList.stream().mapToInt(WeekReport::getEstimate).sum() / weekReportList.size();
        ArrayList<Map<String, Number>> weeksStats = new ArrayList<>();

        for (int weekNumber = 1; weekNumber <= tracking.getCurrentWeek(); weekNumber++) {
            Map<String, Number> weekStats = new HashMap<>();
            weekStats.put("weekNumber", weekNumber);

            int finalWeekNumber = weekNumber;
            Optional<WeekReport> report = weekReportList.stream()
                    .filter(weekReport -> weekReport.getWeek().equals(finalWeekNumber))
                    .findFirst();

            if (!report.isPresent()) {
                weekStats.put("reportId", null);
                weekStats.put("estimate", null);
            } else {
                weekStats.put("reportId", report.get().getId());
                weekStats.put("estimate", report.get().getEstimate());
            }

            weeksStats.add(weekStats);
        }

        statistic.put("weeks", weeksStats);
        statistic.put("avgEstimate", avgEstimate);

        return statistic;
    }
}
