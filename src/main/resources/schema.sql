CREATE TABLE IF NOT EXISTS BET (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(255),
    jackpot_id VARCHAR(36),
    amount DECIMAL(19,2),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS JACKPOT (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255),
    initial_amount DECIMAL(19,2),
    current_amount DECIMAL(19,2),
    contribution_type VARCHAR(20),
    contribution_percent DECIMAL(10,2),
    contribution_rate DECIMAL(19,2),
    reward_type VARCHAR(20),
    reward_percent DECIMAL(10,2),
    reward_rate DECIMAL(19,2),
    reward_limit DECIMAL(19,2)
);

CREATE TABLE IF NOT EXISTS JACKPOT_CONTRIBUTION (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bet_id VARCHAR(36),
    user_id VARCHAR(255),
    jackpot_id VARCHAR(36),
    stake_amount DECIMAL(19,2),
    contribution_amount DECIMAL(19,2),
    current_jackpot_amount DECIMAL(19,2),
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS JACKPOT_REWARD (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    bet_id VARCHAR(36),
    user_id VARCHAR(255),
    jackpot_id VARCHAR(36),
    reward_amount DECIMAL(19,2),
    created_at TIMESTAMP
);
