package com.example.jackpot.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;


@Data
public class JackpotRewardRequestDto {
    @NotBlank(message = "bet ID cannot be blank")
    private String betId;
}
