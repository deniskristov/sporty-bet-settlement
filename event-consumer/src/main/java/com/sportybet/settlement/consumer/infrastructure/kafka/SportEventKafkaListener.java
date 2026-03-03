package com.sportybet.settlement.consumer.infrastructure.kafka;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportybet.settlement.consumer.domain.model.SportEvent;
import com.sportybet.settlement.consumer.application.port.in.ProcessSportEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SportEventKafkaListener {

    private final ProcessSportEventUseCase processSportEventUseCase;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "${kafka.topic.event-outcome}",
            groupId = "${spring.kafka.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(
            @Payload String message,
            @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
            @Header(KafkaHeaders.OFFSET) long offset,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        log.info("Received message from topic={}, partition={}, offset={}, key={}",
                "event-outcome", partition, offset, key);

        try {
            SportEvent sportEvent = objectMapper.readValue(message, SportEvent.class);
            processSportEventUseCase.execute(sportEvent);
        } catch (Exception e) {
            log.error("Error processing message: {}", message, e);
        }
    }
}
