package com.sportybet.settlement.publisher.domain.usecase;

import com.sportybet.settlement.publisher.domain.model.SportEvent;

public interface PublishSportEventUseCase {

    SportEvent execute(SportEvent sportEvent);
}
