package com.example.jackpot.controller;

import com.example.jackpot.dto.BetRequestDto;
import com.example.jackpot.repository.BetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BetsController.class)
class BetsControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    @MockBean BetRepository betRepo;
    @MockBean KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    void placeBet_returns200_andPublishesKafka() throws Exception {
        BetRequestDto dto = new BetRequestDto();
        dto.setUserId("U1");
        dto.setJackpotId("J1");
        dto.setAmount(BigDecimal.TEN.doubleValue());

        mvc.perform(post("/bets")
                .header("X-API-KEY", "my-secret-key")
                .contentType("application/json")
                .content(mapper.writeValueAsString(dto)))
           .andExpect(status().isOk());

        verify(kafkaTemplate).send(any(), any(), any());
    }
}
