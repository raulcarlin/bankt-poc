CREATE TABLE IF NOT EXISTS bank_accounts
(
    id VARCHAR(36) NOT NULL PRIMARY KEY,
    holder VARCHAR(128) DEFAULT NULL,
    type VARCHAR(128) DEFAULT NULL,
    balance INT NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    last_event_version INT NOT NULL
);
