package com.sportybet.settlement.service.infrastructure.rocketmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportybet.settlement.service.domain.model.BetSettlement;
import com.sportybet.settlement.service.domain.usecase.SettleBetUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RocketMQMessageListener(
        topic = "${rocketmq.topic.bet-settlements}",
        consumerGroup = "${rocketmq.consumer.group}"
)
@RequiredArgsConstructor
public class BetSettlementRocketMqListener implements RocketMQListener<String> {

    private final SettleBetUseCase settleBetUseCase;
    private final ObjectMapper objectMapper;

    @Override
    public void onMessage(String message) {
        log.info("Received bet settlements message from RocketMQ");

        try {
            BetSettlement[] settlements = objectMapper.readValue(message, BetSettlement[].class);
            List<BetSettlement> settlementList = Arrays.asList(settlements);

            log.info("Processing {} bet settlement(s)", settlementList.size());

            settlementList.forEach(settleBetUseCase::execute);

            log.info("Successfully processed {} bet settlement(s)", settlementList.size());

        } catch (Exception e) {
            log.error("Failed to process bet settlements message: {}", message, e);
            throw new RuntimeException("Failed to process bet settlements", e);
        }
    }
}
