package com.example.monzun_admin.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "startups", schema = "public")
public class Startup {
    @Id
    @Column(name = "startup_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "logo_id")
    private Attachment logo;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "business_plan")
    private String businessPlan;
    @Column(name = "tasks")
    private String tasks;
    @Column(name = "growth_plan")
    private String growthPlan;
    @Column(name = "use_area")
    private String useArea;
    @Column(name = "points")
    private String points;
    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    public Startup(Long id, Attachment logo, User owner, String name, String description, String businessPlan,
                   String tasks, String growthPlan, String useArea, String points, Date createdAt, Date updatedAt) {
        this.id = id;
        this.logo = logo;
        this.owner = owner;
        this.name = name;
        this.description = description;
        this.businessPlan = businessPlan;
        this.tasks = tasks;
        this.growthPlan = growthPlan;
        this.useArea = useArea;
        this.points = points;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Startup() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Attachment getLogo() {
        return logo;
    }

    public void setLogo(Attachment logo) {
        this.logo = logo;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
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
