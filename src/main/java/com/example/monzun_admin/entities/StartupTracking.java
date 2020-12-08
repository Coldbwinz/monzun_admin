package com.example.monzun_admin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "startup_trackings", schema = "public")
public class StartupTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(targetEntity = Tracking.class, optional = false)
    @JoinColumn(name = "tracking_id", referencedColumnName = "tracking_id")
    private Tracking tracking;
    @ManyToOne(targetEntity = Startup.class, optional = false)
    @JoinColumn(name = "startup_id", referencedColumnName = "startup_id")
    private Startup startup;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "tracker_id", referencedColumnName = "user_id")
    private User tracker;
}
