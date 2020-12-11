package com.example.monzun_admin.request;

import com.example.monzun_admin.validation.rules.UniqueTrackingName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class TrackingRequest {

    @NotNull(message = "name is required")
    @UniqueTrackingName
    private String name;
    private Long logoId;
    private String description;
    @NotNull(message = "is active required")
    private boolean isActive;
    @NotNull(message = "started date is required")
    private Date startedAt;
    @NotNull(message = "ended date is required")
    @FutureOrPresent(message = "ended date should be in future or present")
    private Date endedAt;
}
