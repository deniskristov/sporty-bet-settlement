package com.sportybet.settlement.consumer.domain.port;

import com.sportybet.settlement.consumer.domain.model.BetSettlement;

import java.util.List;

public interface BetSettlementPublisher {

    void publish(List<BetSettlement> betSettlements);
}
