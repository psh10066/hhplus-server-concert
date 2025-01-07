CREATE TABLE user (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid BINARY(16) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE user_wallet (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    balance BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE user_wallet_history (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_wallet_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);