package com.example.jackpot.repository;

import com.example.jackpot.model.Bet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BetRepositoryTest {

    @Autowired
    private BetRepository repo;

    @Test
    void saveAndReadBack() {
        Bet bet = new Bet("U1", "J1", BigDecimal.TEN);
        repo.save(bet);

        assertThat(repo.findById(bet.getId())).isPresent();
    }
}
