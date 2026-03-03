package com.sportybet.settlement.service.application.service;

import com.sportybet.settlement.service.domain.model.BetSettlement;
import com.sportybet.settlement.service.application.port.in.SettleBetUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettleBetService implements SettleBetUseCase {

    @Override
    public void execute(BetSettlement betSettlement) {
        log.info("Settling bet: betId={}, eventId={}, eventName={}",
                betSettlement.getBetId(),
                betSettlement.getEventId(),
                betSettlement.getEventName());

        boolean isWinner = betSettlement.getPredictedWinnerId().equals(betSettlement.getActualWinnerId());

        if (isWinner) {
            log.info("BET WON! betId={}, userId={}, eventId={}, predictedWinnerId={}, actualWinnerId={}, betAmount={}",
                    betSettlement.getBetId(),
                    betSettlement.getUserId(),
                    betSettlement.getEventId(),
                    betSettlement.getPredictedWinnerId(),
                    betSettlement.getActualWinnerId(),
                    betSettlement.getBetAmount());
        } else {
            log.info("BET LOST! betId={}, userId={}, eventId={}, predictedWinnerId={}, actualWinnerId={}, betAmount={}",
                    betSettlement.getBetId(),
                    betSettlement.getUserId(),
                    betSettlement.getEventId(),
                    betSettlement.getPredictedWinnerId(),
                    betSettlement.getActualWinnerId(),
                    betSettlement.getBetAmount());
        }

        log.info("Bet settlement completed: betId={}, result={}",
                betSettlement.getBetId(),
                isWinner ? "WON" : "LOST");
    }
}
