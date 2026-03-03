package com.sportybet.settlement.consumer.application.port.out;

import com.sportybet.settlement.consumer.domain.model.Bet;
import java.util.List;


public interface BetRepository {
    List<Bet> findByEventId(Long eventId);
}
