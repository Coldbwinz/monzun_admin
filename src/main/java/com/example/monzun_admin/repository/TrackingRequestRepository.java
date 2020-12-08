package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.TrackingRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRequestRepository extends JpaRepository<TrackingRequest, Long> {

}
