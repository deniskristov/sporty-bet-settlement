package com.sportybet.settlement.service.domain.usecase;

import com.sportybet.settlement.service.domain.model.BetSettlement;

public interface SettleBetUseCase {

    void execute(BetSettlement betSettlement);
}
