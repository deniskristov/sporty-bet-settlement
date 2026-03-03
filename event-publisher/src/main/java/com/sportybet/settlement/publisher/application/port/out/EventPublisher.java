package com.sportybet.settlement.publisher.application.port.out;

import com.sportybet.settlement.publisher.domain.model.SportEvent;

public interface EventPublisher {

    void publish(SportEvent sportEvent);
}
