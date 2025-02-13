CREATE TABLE user (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    uuid BINARY(16) NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_user_uuid (uuid)
);

CREATE TABLE user_wallet (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    balance BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_user_wallet_user_id (user_id)
);

CREATE TABLE user_wallet_history (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    user_wallet_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_user_wallet_history_user_wallet_id (user_wallet_id)
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
    updated_at DATETIME NOT NULL,
    KEY idx_concert_schedule_concert_id_start_time (concert_id, start_time)
);

CREATE TABLE concert_seat (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    concert_schedule_id BIGINT NOT NULL,
    seat_number INT NOT NULL,
    status VARCHAR(255) NOT NULL,
    version BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    UNIQUE KEY uk_concert_seat_concert_schedule_id_seat_number (concert_schedule_id, seat_number),
    KEY idx_concert_seat_concert_schedule_id (concert_schedule_id)
);

CREATE TABLE reservation (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    concert_id BIGINT NOT NULL,
    concert_seat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    expired_at DATETIME NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_reservation_concert_id (concert_id),
    KEY idx_reservation_concert_seat_id (concert_seat_id),
    KEY idx_reservation_user_id (user_id)
);

CREATE TABLE payment_history (
    id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    reservation_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount BIGINT NOT NULL,
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    KEY idx_payment_history_reservation_id (reservation_id),
    KEY idx_payment_history_user_id (user_id)
);