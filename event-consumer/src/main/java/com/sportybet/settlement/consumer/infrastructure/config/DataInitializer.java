package com.sportybet.settlement.consumer.infrastructure.config;

import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.entity.BetEntity;
import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.repository.SpringDataBetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SpringDataBetRepository betRepository;

    @Override
    public void run(String... args) {
        log.info("Initializing database with test data...");

        List<BetEntity> testBets = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            BetEntity bet = BetEntity.builder()
                    .betId("BET-" + String.format("%04d", i))
                    .userId((long) (1000 + i))
                    .eventId((long) i)
                    .eventMarketId((long) (2000 + i))
                    .eventWinnerId((long) (3000 + i))
                    .betAmount(100 * i)
                    .build();
            testBets.add(bet);
        }

        betRepository.saveAll(testBets);
        log.info("Successfully initialized database with {} bets", testBets.size());

        betRepository.findAll().forEach(bet ->
                log.info("Bet: betId={}, userId={}, eventId={}, eventMarketId={}, eventWinnerId={}, betAmount={}",
                        bet.getBetId(), bet.getUserId(), bet.getEventId(),
                        bet.getEventMarketId(), bet.getEventWinnerId(), bet.getBetAmount())
        );
    }
}
