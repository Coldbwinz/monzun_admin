package com.example.monzun_admin.entities;

import groovy.transform.Generated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "trackings", schema = "public")
public class Tracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tracking_id", nullable = false)
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id", referencedColumnName = "attachment_id")
    private Attachment logo;
    private String description;
    private boolean isActive;
    @Column(name = "started_at")
    private Date startedAt;
    @Column(name = "ended_at", nullable = false)
    private Date endedAt;
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
    @Column(name = "updated_at", insertable = false)
    private Date updatedAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "startup_trackings",
            joinColumns = @JoinColumn(name = "tracking_id"),
            inverseJoinColumns = @JoinColumn(name = "startup_id"))
    private List<Startup> startups;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tracking tracking = (Tracking) o;
        return id.equals(tracking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
