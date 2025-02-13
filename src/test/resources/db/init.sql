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

CREATE TABLE concert (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE concert_schedule (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    concert_id BIGINT NOT NULL,
    start_time DATETIME NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE concert_seat (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    concert_schedule_id BIGINT NOT NULL,
    seat_number INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE reservation (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    concert_id BIGINT NOT NULL,
    concert_seat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    expired_at DATETIME NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);

CREATE TABLE payment_history (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL
);