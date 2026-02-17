package com.sportybet.settlement.publisher.application.service;

import com.sportybet.settlement.publisher.domain.model.SportEvent;
import com.sportybet.settlement.publisher.domain.port.EventPublisher;
import com.sportybet.settlement.publisher.domain.usecase.PublishSportEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublishSportEventService implements PublishSportEventUseCase {

    private final EventPublisher eventPublisher;

    @Override
    public SportEvent execute(SportEvent sportEvent) {
        log.info("Publishing sport event: eventId={}, eventName={}, eventWinnerId={}",
                sportEvent.getEventId(), sportEvent.getEventName(), sportEvent.getEventWinnerId());

        eventPublisher.publish(sportEvent);

        log.info("Sport event published successfully: eventId={}", sportEvent.getEventId());
        return sportEvent;
    }
}
