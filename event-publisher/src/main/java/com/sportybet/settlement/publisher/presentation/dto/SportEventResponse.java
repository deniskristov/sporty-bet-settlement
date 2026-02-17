package com.sportybet.settlement.publisher.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportEventResponse {

    private Long eventId;
    private String eventName;
    private Long eventWinnerId;
    private String status;
}
