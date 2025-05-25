package com.example.jackpot.repository;

import com.example.jackpot.model.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, String> {
}
