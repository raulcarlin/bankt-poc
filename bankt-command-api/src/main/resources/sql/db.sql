CREATE TABLE IF NOT EXISTS bank_accounts
(
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    account_holder VARCHAR(128) DEFAULT NULL,
    account_type VARCHAR(128) DEFAULT NULL,
    balance INT NOT NULL,
    creation_date DATETIME,
    updated_at DATETIME
)
