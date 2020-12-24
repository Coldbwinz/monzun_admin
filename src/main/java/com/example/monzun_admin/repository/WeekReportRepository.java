package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Attachment;
import com.example.monzun_admin.entities.Startup;
import com.example.monzun_admin.entities.Tracking;
import com.example.monzun_admin.entities.WeekReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekReportRepository extends JpaRepository<WeekReport, Long> {
    @Query("select a from Attachment a where a.polytableType = :#{#weekReport.getPolytableType()} " +
            " and a.polytableId = :#{#weekReport.getId()}")
    List<Attachment> getWeekReportAttachments(@Param("weekReport") WeekReport weekReport);

    @Query("SELECT wr FROM WeekReport wr WHERE wr.tracking=:#{#tracking} " +
            "AND wr.startup = :#{#startup} AND wr.week BETWEEN 1 AND :#{#week}")
    List<WeekReport> findWeekReportsByTrackingAndStartupAndForWeek(
            @Param("tracking") Tracking tracking,
            @Param("startup") Startup startup,
            @Param("week") Integer week
    );
}
