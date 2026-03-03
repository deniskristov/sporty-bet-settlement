package com.sportybet.settlement.publisher.presentation.controller;

import com.sportybet.settlement.publisher.domain.model.SportEvent;
import com.sportybet.settlement.publisher.application.port.in.PublishSportEventUseCase;
import com.sportybet.settlement.publisher.presentation.dto.SportEventRequest;
import com.sportybet.settlement.publisher.presentation.dto.SportEventResponse;
import com.sportybet.settlement.publisher.presentation.mapper.SportEventMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/sport-events")
@RequiredArgsConstructor
public class SportEventController {

    private final PublishSportEventUseCase publishSportEventUseCase;
    private final SportEventMapper sportEventMapper;

    @PostMapping
    public ResponseEntity<SportEventResponse> createSportEvent(@Valid @RequestBody SportEventRequest request) {
        log.info("Received request to create sport event: {}", request);

        SportEvent sportEvent = sportEventMapper.toDomain(request);
        SportEvent publishedEvent = publishSportEventUseCase.execute(sportEvent);
        SportEventResponse response = sportEventMapper.toResponse(publishedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
