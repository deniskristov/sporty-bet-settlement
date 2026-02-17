package com.sportybet.settlement.publisher.domain.port;

import com.sportybet.settlement.publisher.domain.model.SportEvent;

public interface EventPublisher {

    void publish(SportEvent sportEvent);
}
