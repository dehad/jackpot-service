package com.example.jackpot.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BetRequestDto {
    @NotBlank(message = "user ID cannot be blank")
    private String userId;
    @NotBlank(message = "jackpot ID cannot be blank")
    private String jackpotId;
    @Min(value = 1, message = "amount must be greater than zero")
    @NotNull(message = "amount cannot be null")
    private Double amount;
}
