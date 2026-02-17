package com.sportybet.settlement.consumer.infrastructure.rocketmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportybet.settlement.consumer.domain.model.BetSettlement;
import com.sportybet.settlement.consumer.domain.port.BetSettlementPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RocketMqBetSettlementPublisher implements BetSettlementPublisher {
    private final RocketMQTemplate rocketMQTemplate;

    @Value("${rocketmq.topic.bet-settlements}")
    private String topic;

    @Override
    public void publish(List<BetSettlement> betSettlements) {
        if (betSettlements == null || betSettlements.isEmpty()) {
            log.debug("No bet settlements to publish");
            return;
        }
        try {
            rocketMQTemplate.convertAndSend(topic, betSettlements);
            log.info("Published {} bet settlement(s) to RocketMQ topic: {}", betSettlements.size(), topic);
            betSettlements.forEach(settlement ->
                    log.debug("Published bet settlement: betId={}, eventId={}", settlement.getBetId(), settlement.getEventId())
            );
        } catch (Exception e) {
            log.error("Failed to publish bet settlements to RocketMQ: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish bet settlements to RocketMQ", e);
        }
    }
}
