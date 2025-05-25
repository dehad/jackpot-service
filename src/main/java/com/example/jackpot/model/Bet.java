package com.example.jackpot.model;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bet {

    @Id
    private String id;
    private String userId;
    private String jackpotId;
    private BigDecimal amount;
    private Instant createdAt;

    public Bet(String userId, String jackpotId, BigDecimal amount) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.jackpotId = jackpotId;
        this.amount = amount;
        this.createdAt = Instant.now();
    }
}
