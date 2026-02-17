package com.sportybet.settlement.consumer.domain.port;

import com.sportybet.settlement.consumer.domain.model.Bet;
import java.util.List;


public interface BetRepository {
    List<Bet> findByEventId(Long eventId);
}
