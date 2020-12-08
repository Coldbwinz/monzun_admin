package com.example.monzun_admin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tracking_requests", schema = "public")
public class TrackingRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(targetEntity = Startup.class)
    @JoinColumn(name = "startup_id", nullable = false)
    private Startup startup;
    @ManyToOne(targetEntity = Tracking.class)
    @JoinColumn(name = "tracking_id", nullable = false)
    private Tracking tracking;
    @Column(name = "created_at")
    private Date createdAt;
}
