package com.sportybet.settlement.publisher.infrastructure.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportybet.settlement.publisher.domain.model.SportEvent;
import com.sportybet.settlement.publisher.application.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.event-outcome}")
    private String topic;

    @Override
    public void publish(SportEvent sportEvent) {
        try {
            String message = objectMapper.writeValueAsString(sportEvent);
            String key = String.valueOf(sportEvent.getEventId());

            kafkaTemplate.send(topic, key, message)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Message sent to topic={}, partition={}, offset={}",
                                    topic,
                                    result.getRecordMetadata().partition(),
                                    result.getRecordMetadata().offset());
                        } else {
                            log.error("Failed to send message to Kafka", ex);
                            throw new RuntimeException("Failed to publish event to Kafka", ex);
                        }
                    });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize SportEvent", e);
            throw new RuntimeException("Failed to serialize event", e);
        }
    }
}
