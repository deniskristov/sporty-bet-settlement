package com.sportybet.settlement.consumer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SportEvent {

    private Long eventId;
    private String eventName;
    private Long eventWinnerId;
}
