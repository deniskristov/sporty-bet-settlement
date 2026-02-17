package com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.repository;

import com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.entity.BetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataBetRepository extends JpaRepository<BetEntity, String> {

    List<BetEntity> findByEventId(Long eventId);
}
