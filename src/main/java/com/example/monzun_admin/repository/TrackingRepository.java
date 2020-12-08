package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Tracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackingRepository extends JpaRepository<Tracking, Long> {
    Tracking findByName(String name);
    boolean existsByName(String name);
}
