package com.example.monzun_admin.dto;

import com.example.monzun_admin.entities.Startup;

import java.util.Date;

public class StartupDTO {
    private Long id;
    private AttachmentShortDTO logo;
    private UserListDTO owner;
    private String name;
    private String description;
    private String businessPlan;
    private String tasks;
    private String growthPlan;
    private String useArea;
    private String points;
    private Date createdAt;
    private Date updatedAt;

    public StartupDTO() {
    }

    public StartupDTO(Startup startup) {
        this.id = startup.getId();
        this.name = startup.getName();
        this.owner = new UserListDTO(startup.getOwner());
        this.logo = startup.getLogo() != null ? new AttachmentShortDTO(startup.getLogo()) : null;
        this.description = startup.getDescription();
        this.businessPlan = startup.getBusinessPlan();
        this.tasks = startup.getTasks();
        this.growthPlan = startup.getGrowthPlan();
        this.points = startup.getPoints();
        this.useArea = startup.getUseArea();
        this.createdAt = startup.getCreatedAt();
        this.updatedAt = startup.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttachmentShortDTO getLogo() {
        return logo;
    }

    public void setLogo(AttachmentShortDTO logo) {
        this.logo = logo;
    }

    public UserListDTO getOwner() {
        return owner;
    }

    public void setOwner(UserListDTO owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBusinessPlan() {
        return businessPlan;
    }

    public void setBusinessPlan(String businessPlan) {
        this.businessPlan = businessPlan;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getGrowthPlan() {
        return growthPlan;
    }

    public void setGrowthPlan(String growthPlan) {
        this.growthPlan = growthPlan;
    }

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
