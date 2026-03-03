package com.sportybet.settlement.service.application.port.in;

import com.sportybet.settlement.service.domain.model.BetSettlement;

public interface SettleBetUseCase {

    void execute(BetSettlement betSettlement);
}
