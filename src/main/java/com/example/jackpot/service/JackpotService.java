package com.example.jackpot.service;

import com.example.jackpot.model.*;
import com.example.jackpot.model.enums.ContributionType;
import com.example.jackpot.model.enums.RewardType;
import com.example.jackpot.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class JackpotService {

    private final JackpotRepository jackpotRepository;
    private final BetRepository betRepository;
    private final Random random = new Random();


    @Transactional
    public void processBet(final Bet bet) {

        final Optional<Jackpot> jackpotOpt = jackpotRepository.findById(bet.getJackpotId());
        if (!jackpotOpt.isPresent()) {
            return; // No matching jackpot
        }
        final Jackpot jackpot = jackpotOpt.get();
        final BigDecimal percent = calculateContributionPercent(jackpot);
        final BigDecimal contribution = bet.getAmount().multiply(percent)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        jackpot.setCurrentAmount(jackpot.getCurrentAmount().add(contribution));

        final JackpotContribution jc = new JackpotContribution();
        jc.setBet(bet);
        jc.setUserId(bet.getUserId());
        jc.setJackpot(jackpot);
        jc.setStakeAmount(bet.getAmount());
        jc.setContributionAmount(contribution);
        jc.setCurrentJackpotAmount(jackpot.getCurrentAmount());
        jackpot.getContributions().add(jc);
        jackpotRepository.save(jackpot);
        log.info("Processed bet: {} for jackpot: {} with contribution: {}", bet.getId(), jackpot.getId(), contribution);
    }

    @Transactional
    public BigDecimal evaluateBet(final String betId) {
        final Bet bet = betRepository.findById(betId)
                .orElseThrow(() -> new IllegalArgumentException("Bet not found"));
        final Jackpot jackpot = jackpotRepository.findById(bet.getJackpotId())
                .orElseThrow(() -> new IllegalArgumentException("Jackpot not found"));

        final BigDecimal chance = calculateRewardChance(jackpot);
        BigDecimal reward = BigDecimal.ZERO;

        final double randomVar = random.nextDouble() * 100;
        if (randomVar < chance.doubleValue()) {
            reward = jackpot.getCurrentAmount();
            jackpot.setCurrentAmount(jackpot.getInitialAmount());

            final JackpotReward jr = new JackpotReward();
            jr.setBet(bet);
            jr.setUserId(bet.getUserId());
            jr.setJackpot(jackpot);
            jr.setRewardAmount(reward);
            jackpot.getRewards().add(jr);
            jackpotRepository.save(jackpot);
        }
        log.info("Evaluated bet: {} for jackpot: {} with chance: {} and reward: {}",
                bet.getId(), jackpot.getId(), chance, reward);
        return reward;
    }

    private BigDecimal calculateContributionPercent(final Jackpot jackpot) {
        if (jackpot.getContributionType() == ContributionType.FIXED) {
            return jackpot.getContributionPercent();
        } else if (jackpot.getContributionType() == ContributionType.VARIABLE) {
            // Variable: initial high then decreases
            final BigDecimal rate = jackpot.getContributionRate();
            final BigDecimal dynamic = jackpot.getContributionPercent()
                    .subtract(jackpot.getCurrentAmount().divide(rate, 2, RoundingMode.HALF_UP));
            return dynamic.max(BigDecimal.ONE);
        } else {
            throw new UnsupportedOperationException("Unsupported contribution type: " + jackpot.getContributionType());
        }
    }

    private BigDecimal calculateRewardChance(final Jackpot jackpot) {
        if (jackpot.getRewardType() == RewardType.FIXED) {
            return jackpot.getRewardPercent();
        } else if (jackpot.getRewardType() == RewardType.VARIABLE) {
            if (jackpot.getCurrentAmount().compareTo(jackpot.getRewardLimit()) >= 0) {
                return BigDecimal.valueOf(100);
            }
            final BigDecimal chance = jackpot.getRewardPercent()
                    .add(jackpot.getCurrentAmount().divide(jackpot.getRewardRate(), 2, RoundingMode.HALF_UP));
            return chance.min(BigDecimal.valueOf(100));
        } else {
            throw new UnsupportedOperationException("Unsupported reward type: " + jackpot.getRewardType());
        }
    }

}
