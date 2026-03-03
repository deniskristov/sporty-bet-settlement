package com.sportybet.settlement.consumer.integration;

import com.sportybet.settlement.consumer.domain.model.SportEvent;
import com.sportybet.settlement.consumer.application.port.out.BetSettlementPublisher;
import com.sportybet.settlement.consumer.application.port.in.ProcessSportEventUseCase;
import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.entity.BetEntity;
import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.repository.SpringDataBetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"event-outcome"})
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "kafka.topic.event-outcome=event-outcome",
        "rocketmq.name-server=localhost:9876",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class EventConsumerIntegrationTest {

    @Autowired
    private ProcessSportEventUseCase processSportEventUseCase;

    @MockBean
    private BetSettlementPublisher betSettlementPublisher;

    @Autowired
    private SpringDataBetRepository betRepository;

    @BeforeEach
    void setUp() {
        // Clear any existing data and add test bet
        BetEntity testBet = BetEntity.builder()
                .betId("BET-TEST-001")
                .userId(1001L)
                .eventId(100L)
                .eventMarketId(2001L)
                .eventWinnerId(3001L)
                .betAmount(500)
                .build();

        betRepository.save(testBet);
    }

    @Test
    void shouldProcessSportEventAndPublishMatchingBets() {
        // Given
        SportEvent sportEvent = SportEvent.builder()
                .eventId(100L)
                .eventName("Test Event")
                .eventWinnerId(3001L)
                .build();

        // When
        processSportEventUseCase.execute(sportEvent);

        // Then
        verify(betSettlementPublisher, times(1)).publish(anyList());
    }

    @Test
    void shouldNotPublishWhenNoMatchingBets() {
        // Given
        SportEvent sportEvent = SportEvent.builder()
                .eventId(999L)
                .eventName("Event Without Bets")
                .eventWinnerId(3001L)
                .build();

        // When
        processSportEventUseCase.execute(sportEvent);

        // Then
        verify(betSettlementPublisher, never()).publish(anyList());
    }

    @Test
    void shouldFindMultipleBetsForSameEvent() {
        // Given - Add multiple bets for same event
        BetEntity bet1 = BetEntity.builder()
                .betId("BET-MULTI-001")
                .userId(2001L)
                .eventId(200L)
                .eventMarketId(2002L)
                .eventWinnerId(3002L)
                .betAmount(100)
                .build();

        BetEntity bet2 = BetEntity.builder()
                .betId("BET-MULTI-002")
                .userId(2002L)
                .eventId(200L)
                .eventMarketId(2002L)
                .eventWinnerId(3003L)
                .betAmount(200)
                .build();

        betRepository.saveAll(Arrays.asList(bet1, bet2));

        SportEvent sportEvent = SportEvent.builder()
                .eventId(200L)
                .eventName("Multi Bet Event")
                .eventWinnerId(3002L)
                .build();

        // When
        processSportEventUseCase.execute(sportEvent);

        // Then
        verify(betSettlementPublisher, times(1)).publish(argThat(list ->
            list != null && list.size() == 2
        ));
    }
}
