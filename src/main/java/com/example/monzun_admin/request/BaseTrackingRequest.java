package com.example.monzun_admin.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
abstract public class BaseTrackingRequest {
    private String name;
    private Long logoId;
    private String description;
    private boolean isActive;
    private LocalDate startedAt;
    private LocalDate endedAt;
}
