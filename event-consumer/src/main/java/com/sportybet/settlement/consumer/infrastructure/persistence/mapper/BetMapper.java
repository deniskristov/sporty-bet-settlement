package com.sportybet.settlement.consumer.infrastructure.persistence.mapper;

import com.sportybet.settlement.consumer.domain.model.Bet;
import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.entity.BetEntity;
import org.springframework.stereotype.Component;

@Component
public class BetMapper {
    public Bet toDomain(BetEntity betEntity) {
        return Bet.builder()
                .betId(betEntity.getBetId())
                .userId(betEntity.getUserId())
                .eventId(betEntity.getEventId())
                .eventMarketId(betEntity.getEventMarketId())
                .eventWinnerId(betEntity.getEventWinnerId())
                .betAmount(betEntity.getBetAmount())
                .build();
    }
}
