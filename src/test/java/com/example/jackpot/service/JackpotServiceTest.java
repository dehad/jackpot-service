package com.example.jackpot.service;

import com.example.jackpot.model.*;
import com.example.jackpot.model.enums.ContributionType;
import com.example.jackpot.model.enums.RewardType;
import com.example.jackpot.repository.BetRepository;
import com.example.jackpot.repository.JackpotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class JackpotServiceTest {

    private JackpotService jackpotService;
    private JackpotRepository jackpotRepo;
    private BetRepository betRepo;

    private Jackpot jackpot;

    @BeforeEach
    void setUp() throws Exception {
        jackpotRepo = mock(JackpotRepository.class);
        betRepo = mock(BetRepository.class);
        jackpotService = new JackpotService(jackpotRepo, betRepo);

        // Build jackpot with non‑null collections
        jackpot = Jackpot.builder()
                .id("J1")
                .name("JP")
                .initialAmount(BigDecimal.ZERO)
                .currentAmount(BigDecimal.ZERO)
                .contributionType(ContributionType.FIXED)
                .contributionPercent(BigDecimal.valueOf(5))
                .rewardType(RewardType.FIXED)
                .rewardPercent(BigDecimal.valueOf(100))   // 100 % шанс → тестът е детерминиран
                .contributions(new ArrayList<>())
                .rewards(new ArrayList<>())
                .build();

        when(jackpotRepo.findById("J1")).thenReturn(Optional.of(jackpot));
    }

    @Test
    void processBet_addsContributionAndSavesJackpot() {
        Bet bet = new Bet("U1", "J1", BigDecimal.TEN);

        jackpotService.processBet(bet);

        ArgumentCaptor<Jackpot> cap = ArgumentCaptor.forClass(Jackpot.class);
        verify(jackpotRepo).save(cap.capture());

        Jackpot saved = cap.getValue();
        assertThat(saved.getContributions()).hasSize(1);
        assertThat(saved.getContributions().get(0).getContributionAmount())
                .isEqualByComparingTo("0.50");
    }

    @Test
    void evaluateBet_whenChanceIsCertain_addsRewardAndResetsPool() {
        // Prepare bet stub
        Bet bet = new Bet("U1", "J1", BigDecimal.ONE);
        when(betRepo.findById(bet.getId())).thenReturn(Optional.of(bet));

        // Give jackpot some money
        jackpot.setCurrentAmount(BigDecimal.valueOf(15));

        BigDecimal reward = jackpotService.evaluateBet(bet.getId());

        assertThat(reward).isEqualByComparingTo("15");

        ArgumentCaptor<Jackpot> cap = ArgumentCaptor.forClass(Jackpot.class);
        verify(jackpotRepo).save(cap.capture());   // once in processBet, once here
        Jackpot saved = cap.getValue();
        assertThat(saved.getRewards()).hasSize(1);
        assertThat(saved.getCurrentAmount()).isEqualByComparingTo("0");
    }
}
