package com.example.jackpot.service;

import com.example.jackpot.model.Bet;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BetListener {

    private final JackpotService jackpotService;

    public BetListener(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    @KafkaListener(topics = "jackpot-bets", groupId = "jackpot-service")
    public void handle(Bet bet) {
        jackpotService.processBet(bet);
    }
}
