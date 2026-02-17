package com.sportybet.settlement.publisher.presentation.mapper;

import com.sportybet.settlement.publisher.domain.model.SportEvent;
import com.sportybet.settlement.publisher.presentation.dto.SportEventRequest;
import com.sportybet.settlement.publisher.presentation.dto.SportEventResponse;
import org.springframework.stereotype.Component;

@Component
public class SportEventMapper {

    public SportEvent toDomain(SportEventRequest request) {
        return SportEvent.builder()
                .eventId(request.getEventId())
                .eventName(request.getEventName())
                .eventWinnerId(request.getEventWinnerId())
                .build();
    }

    public SportEventResponse toResponse(SportEvent sportEvent) {
        return SportEventResponse.builder()
                .eventId(sportEvent.getEventId())
                .eventName(sportEvent.getEventName())
                .eventWinnerId(sportEvent.getEventWinnerId())
                .status("PUBLISHED")
                .build();
    }
}
