package com.sportybet.settlement.consumer.application.port.out;

import com.sportybet.settlement.consumer.domain.model.BetSettlement;

import java.util.List;

public interface BetSettlementPublisher {

    void publish(List<BetSettlement> betSettlements);
}
