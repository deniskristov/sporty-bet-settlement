package com.sportybet.settlement.publisher.application.port.in;

import com.sportybet.settlement.publisher.domain.model.SportEvent;

public interface PublishSportEventUseCase {

    SportEvent execute(SportEvent sportEvent);
}
