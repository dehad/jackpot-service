package com.example.jackpot.dto;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class BetResponseDto {
    private String id;
    private String userId;
    private String jackpotId;
    private BigDecimal amount;
}
