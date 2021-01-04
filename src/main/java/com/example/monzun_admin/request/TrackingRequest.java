package com.example.monzun_admin.request;

import com.example.monzun_admin.validation.rules.UniqueTrackingName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TrackingRequest {

    @NotNull(message = "name is required")
    @UniqueTrackingName
    @ApiModelProperty(required = true)
    private String name;
    private Long logoId;
    private String description;
    @NotNull(message = "is active required")
    @ApiModelProperty(required = true)
    private boolean isActive;
    @NotNull(message = "started date is required")
    @ApiModelProperty(required = true)
    private LocalDateTime startedAt;
    @NotNull(message = "ended date is required")
    @ApiModelProperty(required = true)
    @FutureOrPresent(message = "ended date should be in future or present")
    private LocalDateTime endedAt;
}
