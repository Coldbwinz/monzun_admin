package com.example.monzun_admin.entities;

import com.example.monzun_admin.dto.AttachmentShortDTO;
import com.example.monzun_admin.enums.AttachmentPolytableTypeConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "startups", schema = "public")
public class Startup {
    @Id
    @Column(name = "startup_id", updatable = false, nullable = false)
    @SequenceGenerator(name = "startups_seq",
            sequenceName = "startups_startup_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "startups_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "logo_id")
    private Attachment logo;
    @ManyToOne()
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
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Transient
    private List<AttachmentShortDTO> attachmentsDTO;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "startup_trackings",
            joinColumns = @JoinColumn(name = "startup_id"),
            inverseJoinColumns = @JoinColumn(name = "tracking_id"))
    private List<Tracking> trackings;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Startup startup = (Startup) o;
        return id.equals(startup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Polytable type стартапа
     *
     * @return String
     */
    public String getPolytableType() {
        return AttachmentPolytableTypeConstants.STARTUP.getType();
    }
}
