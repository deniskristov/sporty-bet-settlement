package com.sportybet.settlement.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetSettlement {

    private String betId;
    private Long userId;
    private Long eventId;
    private String eventName;
    private Long eventMarketId;
    private Long predictedWinnerId;
    private Long actualWinnerId;
    private Integer betAmount;
}
