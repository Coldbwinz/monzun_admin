package com.example.monzun_admin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    @Column(name = "tracking_id", nullable = false, updatable = false)
    @SequenceGenerator(name = "trackings_seq",
            sequenceName = "trackings_tracking_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "trackings_seq")
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logo_id", referencedColumnName = "attachment_id")
    private Attachment logo;
    private String description;
    @Column(name = "is_active")
    private boolean active;
    @Column(name = "started_at")
    private LocalDate startedAt;
    @Column(name = "ended_at", nullable = false)
    private LocalDate endedAt;
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;
    @ManyToMany(fetch = FetchType.LAZY)
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

    /**
     * Получить текущую неделю набора
     *
     * @return int
     */
    public int getCurrentWeek() {
        if (LocalDate.now().isAfter(endedAt)) {
            return getWeeksCount();
        }

        long diffInDays = ChronoUnit.DAYS.between(startedAt, LocalDateTime.now());
        System.out.println();
        return diffInDays < 0 ? 0 : diffInDays <= 7 ? 1 : (int) Math.ceil((diffInDays / 7.0));
    }

    /**
     * Количество недель в наборе
     * @return int
     */
    public int getWeeksCount() {
        return (int) ChronoUnit.WEEKS.between(startedAt, endedAt);
    }
}
