package com.example.monzun_admin.request;

import com.example.monzun_admin.validation.rules.UniqueTrackingName;
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
public class TrackingRequest extends BaseTrackingRequest{

    @NotNull(message = "Name is required")
    @UniqueTrackingName
    @ApiModelProperty(required = true)
    private String name;
    private Long logoId;
    private String description;
    @NotNull(message = "Is active required")
    @ApiModelProperty(required = true)
    private boolean active;
    @NotNull(message = "Started date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @ApiModelProperty(required = true)
    private LocalDate startedAt;
    @NotNull(message = "Ended date is required")
    @ApiModelProperty(required = true)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate endedAt;
}
