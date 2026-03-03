package com.sportybet.settlement.consumer.application.port.in;

import com.sportybet.settlement.consumer.domain.model.SportEvent;

public interface ProcessSportEventUseCase {

    void execute(SportEvent sportEvent);
}
