package com.example.monzun_admin.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ExistsTrackingRequest extends TrackingRequest {
    @NotNull(message = "name is required")
    @ApiModelProperty(required = true)
    private String name;
    private Long logoId;
    private String description;
    @NotNull(message = "is active required")
    @ApiModelProperty(required = true)
    private boolean isActive;
    @NotNull(message = "started date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @ApiModelProperty(required = true)
    private LocalDate startedAt;
    @NotNull(message = "ended date is required")
    @ApiModelProperty(required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDate endedAt;
}
