package com.example.monzun_admin.jobs;

import com.example.monzun_admin.entities.Mail;
import com.example.monzun_admin.service.EmailService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.mail.MessagingException;

public class EmailJob extends QuartzJobBean {
    private final EmailService service;

    public EmailJob(EmailService service) {
        this.service = service;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        Mail mail = (Mail) jobDataMap.get("mail");
        String template = jobDataMap.getString("template");

        try {
            service.sendEmail(mail, template);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
