package com.sportybet.settlement.publisher.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportEventRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;

    @NotBlank(message = "Event name is required")
    private String eventName;

    @NotNull(message = "Event winner ID is required")
    private Long eventWinnerId;
}
