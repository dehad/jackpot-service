package com.example.jackpot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JackpotContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Bet bet;
    private String userId;
    @ManyToOne
    private Jackpot jackpot;
    private BigDecimal stakeAmount;
    private BigDecimal contributionAmount;
    private BigDecimal currentJackpotAmount;
    private Instant createdAt = Instant.now();


    // getters and setters omitted for brevity
}
