package com.sportybet.settlement.consumer.application.service;


import com.sportybet.settlement.consumer.domain.model.Bet;
import com.sportybet.settlement.consumer.domain.model.BetSettlement;
import com.sportybet.settlement.consumer.domain.model.SportEvent;

import com.sportybet.settlement.consumer.application.port.out.BetRepository;
import com.sportybet.settlement.consumer.application.port.out.BetSettlementPublisher;
import com.sportybet.settlement.consumer.application.port.in.ProcessSportEventUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessSportEventService implements ProcessSportEventUseCase {

    private final BetRepository betRepository;
    private final BetSettlementPublisher betSettlementPublisher;

    @Override
    public void execute(SportEvent sportEvent) {
        log.info("Processing sport event: eventId={}, eventName={}, eventWinnerId={}",
                sportEvent.getEventId(),
                sportEvent.getEventName(),
                sportEvent.getEventWinnerId());

        List<Bet> matchingBets = betRepository.findByEventId(sportEvent.getEventId());

        if (matchingBets.isEmpty()) {
            log.info("No bets found for eventId={}", sportEvent.getEventId());
        } else {
            log.info("Found {} bet(s) for eventId={}", matchingBets.size(), sportEvent.getEventId());
            matchingBets.forEach(bet ->
                    log.info("Matching bet: betId={}, userId={}, eventId={}, eventMarketId={}, predictedWinnerId={}, betAmount={}",
                            bet.getBetId(), bet.getUserId(), bet.getEventId(),
                            bet.getEventMarketId(), bet.getEventWinnerId(), bet.getBetAmount())
            );

            // Convert Bets to BetSettlements with actual winner info
            List<BetSettlement> betSettlements = matchingBets.stream()
                    .map(bet -> BetSettlement.builder()
                            .betId(bet.getBetId())
                            .userId(bet.getUserId())
                            .eventId(bet.getEventId())
                            .eventName(sportEvent.getEventName())
                            .eventMarketId(bet.getEventMarketId())
                            .predictedWinnerId(bet.getEventWinnerId())
                            .actualWinnerId(sportEvent.getEventWinnerId())
                            .betAmount(bet.getBetAmount())
                            .build())
                    .collect(Collectors.toList());

            betSettlementPublisher.publish(betSettlements);
        }

        log.info("Sport event processed successfully: eventId={}", sportEvent.getEventId());
    }
}
