package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.StartupTracking;
import com.example.monzun_admin.entities.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StartupTrackingRepository extends JpaRepository<StartupTracking, Long> {
    Optional<StartupTracking> findByTrackingAndStartup(Tracking tracking, Startup startup);
}
