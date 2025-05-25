INSERT INTO jackpot (id, name, initial_amount, current_amount, contribution_type, contribution_percent, contribution_rate, reward_type, reward_percent, reward_rate, reward_limit)
VALUES ('J1', 'Default Jackpot', 0, 0, 'FIXED', 5, NULL, 'FIXED', 50, NULL, NULL);
-- Sample jackpot with VARIABLE contribution
INSERT INTO JACKPOT (
    id, name, initial_amount, current_amount,
    contribution_type, contribution_percent, contribution_rate,
    reward_type, reward_percent, reward_rate, reward_limit
) VALUES (
             'J2',
             'Variable Contribution Jackpot',
             0,                       -- initial_amount
             0,                       -- current_amount
             'VARIABLE',              -- contribution_type
             10.00,                   -- contribution_percent (начален %)
             1000.00,                 -- contribution_rate  (делител за динамичното намаляване)
             'FIXED',                 -- reward_type  (примерно фиксиран шанс)
             1.00,                    -- reward_percent
             NULL,                    -- reward_rate (не е нужен при FIXED)
             NULL                     -- reward_limit
         );
-- Sample jackpot with VARIABLE reward
INSERT INTO JACKPOT (
    id, name, initial_amount, current_amount,
    contribution_type, contribution_percent, contribution_rate,
    reward_type, reward_percent, reward_rate, reward_limit
) VALUES (
             'J3',
             'Variable Reward Jackpot',
             0,                        -- initial_amount
             0,                        -- current_amount
             'FIXED',                  -- contribution_type (примерно фиксиран 5 %)
             5.00,                     -- contribution_percent
             NULL,                     -- contribution_rate (не е нужен при FIXED)
             'VARIABLE',               -- reward_type
             0.50,                     -- reward_percent (начална вероятност в %)
             100.00,                   -- reward_rate  (делител за нарастващия шанс)
             50000.00                  -- reward_limit (над тази сума шансът = 100 %)
         );


-- Sample Bets
INSERT INTO BET (id, user_id, jackpot_id, amount, created_at)
VALUES ('B1', 'U1', 'J1', 10.00, CURRENT_TIMESTAMP);

INSERT INTO BET (id, user_id, jackpot_id, amount, created_at)
VALUES ('B2', 'U2', 'J1', 20.00, CURRENT_TIMESTAMP);

INSERT INTO BET (id, user_id, jackpot_id, amount, created_at)
VALUES ('B3', 'U3', 'J1', 15.00, CURRENT_TIMESTAMP);


-- Sample Contribution (assumed 5% of stake)
INSERT INTO JACKPOT_CONTRIBUTION (bet_id, user_id, jackpot_id, stake_amount, contribution_amount, current_jackpot_amount, created_at)
VALUES ('B1', 'U1', 'J1', 10.00, 0.50, 0.50, CURRENT_TIMESTAMP);

-- Sample Reward
INSERT INTO JACKPOT_REWARD (bet_id, user_id, jackpot_id, reward_amount, created_at)
VALUES ('B2', 'U2', 'J1', 5.00, CURRENT_TIMESTAMP);
