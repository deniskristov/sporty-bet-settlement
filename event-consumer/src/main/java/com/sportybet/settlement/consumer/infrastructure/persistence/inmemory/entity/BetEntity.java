package com.sportybet.settlement.consumer.infrastructure.persistence.inmemory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bets")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetEntity {

    @Id
    private String betId;
    private Long userId;
    private Long eventId;
    private Long eventMarketId;
    private Long eventWinnerId;
    private Integer betAmount;
}
