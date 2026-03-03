package com.sportybet.settlement.consumer.infrastructure.persistence;

import com.sportybet.settlement.consumer.domain.model.Bet;
import com.sportybet.settlement.consumer.application.port.out.BetRepository;
import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.repository.SpringDataBetRepository;
import com.sportybet.settlement.consumer.infrastructure.persistence.mapper.BetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BetPersistenceAdapter implements BetRepository {
    private final SpringDataBetRepository springDataBetRepository;
    private final BetMapper betMapper;

    @Override
    public List<Bet> findByEventId(Long eventId) {
        return springDataBetRepository.findByEventId(eventId)
                .stream().map(betMapper::toDomain).toList();
    }
}
