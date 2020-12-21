package com.example.monzun_admin.repository;

import com.example.monzun_admin.entities.Attachment;
import com.example.monzun_admin.entities.WeekReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeekReportRepositoryWithJOOQ {
    List<Attachment> getWeekReportAttachments(WeekReport weekReport);
}
