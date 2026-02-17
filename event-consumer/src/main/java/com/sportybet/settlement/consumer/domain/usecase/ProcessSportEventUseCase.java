package com.sportybet.settlement.consumer.domain.usecase;

import com.sportybet.settlement.consumer.domain.model.SportEvent;

public interface ProcessSportEventUseCase {

    void execute(SportEvent sportEvent);
}
