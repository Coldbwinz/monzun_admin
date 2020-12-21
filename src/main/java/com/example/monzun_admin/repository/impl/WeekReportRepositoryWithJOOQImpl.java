package com.example.monzun_admin.repository.impl;

import com.example.monzun_admin.entities.Attachment;
import com.example.monzun_admin.entities.WeekReport;
import com.example.monzun_admin.enums.AttachmentPolytableTypeConstants;
import com.example.monzun_admin.repository.WeekReportRepositoryWithJOOQ;
import org.jooq.DSLContext;

import java.util.List;

public class WeekReportRepositoryWithJOOQImpl implements WeekReportRepositoryWithJOOQ {
    private final DSLContext jooq;

    public WeekReportRepositoryWithJOOQImpl(DSLContext jooq) {
        this.jooq = jooq;
    }

    @Override
    public List<Attachment> getWeekReportAttachments(WeekReport weekReport) {
        return jooq.fetch(
                "SELECT DISTINCT(a.*) " +
                        "FROM attachments AS a " +
                        "WHERE a.polytable_id = " + weekReport.getId() + " " +
                        "AND a.polytable_type = '" + AttachmentPolytableTypeConstants.WEEK_REPORT.getType() + "'")
                .into(Attachment.class);
    }
}
