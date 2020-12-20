package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.entities.TrackingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackingRequestRepository extends JpaRepository<TrackingRequest, Long> {
    List<TrackingRequest> findAllByTracking(Tracking tracking);
}
