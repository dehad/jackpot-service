package com.example.jackpot.model;

import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JackpotReward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Bet bet;
    private String userId;
    @ManyToOne
    private Jackpot jackpot;
    private BigDecimal rewardAmount;
    private Instant createdAt = Instant.now();

    // getters and setters omitted for brevity
}
