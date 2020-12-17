package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Attachment;
import com.example.monzun_admin.entities.Startup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StartupRepository extends JpaRepository<Startup, Long> {
    @Query("select a from Attachment a where a.polytableType = :#{#startup.getPolytableType()} " +
            " and a.polytableId = :#{#startup.getId()}")
    List<Attachment> getStartupAttachments(@Param("startup") Startup startup);
}
