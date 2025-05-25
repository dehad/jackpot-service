package com.example.jackpot.model;

import com.example.jackpot.model.enums.ContributionType;
import com.example.jackpot.model.enums.RewardType;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jackpot {

    @Id
    private String id;
    private String name;
    private BigDecimal initialAmount;
    private BigDecimal currentAmount;

    @Enumerated(EnumType.STRING)
    private ContributionType contributionType;
    private BigDecimal contributionPercent;
    private BigDecimal contributionRate;

    @Enumerated(EnumType.STRING)
    private RewardType rewardType;
    private BigDecimal rewardPercent;
    private BigDecimal rewardRate;
    private BigDecimal rewardLimit;

    @OneToMany(mappedBy = "jackpot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JackpotReward> rewards;
    @OneToMany(mappedBy = "jackpot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<JackpotContribution> contributions;
}
