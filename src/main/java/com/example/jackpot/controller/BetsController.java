package com.example.jackpot.controller;

import com.example.jackpot.dto.BetRequestDto;
import com.example.jackpot.dto.BetResponseDto;
import com.example.jackpot.model.Bet;
import com.example.jackpot.repository.BetRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bets")
public class BetsController {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final BetRepository betRepository;

    public BetsController(KafkaTemplate<String, Object> kafkaTemplate,
                          BetRepository betRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.betRepository = betRepository;
    }

    @PostMapping
    public BetResponseDto placeBet(@RequestBody @Valid BetRequestDto request) {
        Bet bet = new Bet(request.getUserId(), request.getJackpotId(),
                BigDecimal.valueOf(request.getAmount())
        );
        betRepository.save(bet);
        kafkaTemplate.send("jackpot-bets", bet.getId(), bet);

        return BetResponseDto.builder()
                .id(bet.getId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .amount(bet.getAmount())
                .build();
    }

    @GetMapping
    public List<BetResponseDto> getAllBets() {

        return betRepository.findAll().stream().map(bet -> BetResponseDto.builder()
                .id(bet.getId())
                .userId(bet.getUserId())
                .jackpotId(bet.getJackpotId())
                .amount(bet.getAmount())
                .build()).collect(Collectors.toList());

    }
}
