package com.sportybet.settlement.service.integration;

import com.sportybet.settlement.service.domain.model.BetSettlement;
import com.sportybet.settlement.service.application.port.in.SettleBetUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.TestPropertySource;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(OutputCaptureExtension.class)
@TestPropertySource(properties = {
        "rocketmq.name-server=localhost:9876",
        "rocketmq.consumer.group=test-consumer-group",
        "rocketmq.topic.bet-settlements=bet-settlements"
})
class BettingSettlementIntegrationTest {

    @Autowired
    private SettleBetUseCase settleBetUseCase;

    @Test
    void shouldSettleWinningBet(CapturedOutput output) {
        // Given - A bet where predicted winner matches actual winner
        BetSettlement betSettlement = BetSettlement.builder()
                .betId("BET-WIN-001")
                .userId(1001L)
                .eventId(100L)
                .eventName("Winning Bet Event")
                .eventMarketId(2001L)
                .predictedWinnerId(3001L)
                .actualWinnerId(3001L)
                .betAmount(500)
                .build();

        // When
        settleBetUseCase.execute(betSettlement);

        // Then
        assertThat(output.getOut())
                .contains("Settling bet: betId=BET-WIN-001")
                .contains("BET WON!")
                .contains("result=WON");
    }

    @Test
    void shouldSettleLosingBet(CapturedOutput output) {
        // Given - A bet where predicted winner does NOT match actual winner
        BetSettlement betSettlement = BetSettlement.builder()
                .betId("BET-LOSE-001")
                .userId(1002L)
                .eventId(101L)
                .eventName("Losing Bet Event")
                .eventMarketId(2002L)
                .predictedWinnerId(3001L)
                .actualWinnerId(3002L)
                .betAmount(300)
                .build();

        // When
        settleBetUseCase.execute(betSettlement);

        // Then
        assertThat(output.getOut())
                .contains("Settling bet: betId=BET-LOSE-001")
                .contains("BET LOST!")
                .contains("result=LOST");
    }

    @Test
    void shouldLogCorrectBetDetails(CapturedOutput output) {
        // Given
        BetSettlement betSettlement = BetSettlement.builder()
                .betId("BET-DETAIL-001")
                .userId(1003L)
                .eventId(102L)
                .eventName("Detail Test Event")
                .eventMarketId(2003L)
                .predictedWinnerId(3001L)
                .actualWinnerId(3001L)
                .betAmount(1000)
                .build();

        // When
        settleBetUseCase.execute(betSettlement);

        // Then
        assertThat(output.getOut())
                .contains("betId=BET-DETAIL-001")
                .contains("userId=1003")
                .contains("eventId=102")
                .contains("predictedWinnerId=3001")
                .contains("actualWinnerId=3001")
                .contains("betAmount=1000");
    }

    @Test
    void shouldHandleMultipleBetSettlements(CapturedOutput output) {
        // Given
        BetSettlement winningBet = BetSettlement.builder()
                .betId("BET-MULTI-WIN")
                .userId(2001L)
                .eventId(200L)
                .eventName("Multi Test")
                .eventMarketId(3001L)
                .predictedWinnerId(4001L)
                .actualWinnerId(4001L)
                .betAmount(250)
                .build();

        BetSettlement losingBet = BetSettlement.builder()
                .betId("BET-MULTI-LOSE")
                .userId(2002L)
                .eventId(200L)
                .eventName("Multi Test")
                .eventMarketId(3001L)
                .predictedWinnerId(4002L)
                .actualWinnerId(4001L)
                .betAmount(750)
                .build();

        // When
        settleBetUseCase.execute(winningBet);
        settleBetUseCase.execute(losingBet);

        // Then
        assertThat(output.getOut())
                .contains("BET WON!")
                .contains("BET LOST!")
                .contains("BET-MULTI-WIN")
                .contains("BET-MULTI-LOSE");
    }
}
