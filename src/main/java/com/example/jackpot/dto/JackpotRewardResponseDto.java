package com.example.jackpot.dto;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class JackpotRewardResponseDto {
    private final BigDecimal rewardAmount;
}
