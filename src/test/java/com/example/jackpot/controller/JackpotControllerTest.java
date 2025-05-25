package com.example.jackpot.controller;

import com.example.jackpot.dto.JackpotRewardRequestDto;
import com.example.jackpot.service.JackpotService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JackpotController.class)
class JackpotControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean JackpotService jackpotService;

    @Test
    void evaluateBet_returnsReward() throws Exception {
        when(jackpotService.evaluateBet("B1")).thenReturn(BigDecimal.TEN);

        JackpotRewardRequestDto req = new JackpotRewardRequestDto();
        req.setBetId("B1");

        mvc.perform(post("/jackpots/rewards/evaluate-bet")
                .header("X-API-KEY", "my-secret-key")
                .contentType("application/json")
                .content(mapper.writeValueAsString(req)))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.rewardAmount").value(10));
    }
}
