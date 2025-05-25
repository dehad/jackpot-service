package com.example.jackpot.controller;

import com.example.jackpot.dto.JackpotRewardRequestDto;
import com.example.jackpot.dto.JackpotRewardResponseDto;
import com.example.jackpot.service.JackpotService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/jackpots")
public class JackpotController {

    private final JackpotService jackpotService;

    public JackpotController(JackpotService jackpotService) {
        this.jackpotService = jackpotService;
    }

    @PostMapping("/rewards/evaluate-bet")
    public JackpotRewardResponseDto evaluate(
            @RequestBody @Valid JackpotRewardRequestDto jackpotRewardRequestDto) {

        return new JackpotRewardResponseDto(
                jackpotService.evaluateBet(jackpotRewardRequestDto.getBetId()));
    }
}
