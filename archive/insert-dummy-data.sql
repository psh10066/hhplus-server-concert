-- 높은 재귀(반복) 횟수를 허용하도록 설정
SET SESSION cte_max_recursion_depth = 10000000;

TRUNCATE user;
INSERT INTO user (id, uuid, name, created_at, updated_at)
WITH RECURSIVE cte (n) AS ( SELECT 1 UNION ALL SELECT n + 1 FROM cte
    WHERE n < 10000000 -- 생성하고 싶은 더미 데이터의 개수
)
SELECT
    n AS id,
    UNHEX(REPLACE(UUID(), '-', '')) AS uuid,
    CONCAT('유저', n) AS name,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS created_at,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS updated_at
FROM cte;

TRUNCATE concert_schedule;
INSERT INTO concert_schedule (id, concert_id, start_time, created_at, updated_at)
WITH RECURSIVE cte (n) AS ( SELECT 1 UNION ALL SELECT n + 1 FROM cte
    WHERE n < 100000 -- 생성하고 싶은 더미 데이터의 개수
)
SELECT
    n AS id,
    CEIL(n / 10) AS concert_id,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 10000000 + FLOOR(RAND() * 31536000)) AS start_time,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS created_at,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS updated_at
FROM cte;

TRUNCATE concert_seat;
INSERT INTO concert_seat (id, concert_schedule_id, seat_number, status, version, created_at, updated_at)
WITH RECURSIVE cte (n) AS ( SELECT 1 UNION ALL SELECT n + 1 FROM cte
    WHERE n < 5000000 -- 생성하고 싶은 더미 데이터의 개수
)
SELECT
    n AS id,
    CEIL(n / 50) AS concert_schedule_id,
    n % 50 + 1 AS seat_number,
    'AVAILABLE' AS status,
    0 AS version,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS created_at,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS updated_at
FROM cte;

TRUNCATE reservation;
INSERT INTO reservation (id, concert_id, concert_seat_id, user_id, status, expired_at, created_at, updated_at)
WITH RECURSIVE cte (n) AS ( SELECT 1 UNION ALL SELECT n + 1 FROM cte
    WHERE n < 10000000 -- 생성하고 싶은 더미 데이터의 개수
)
SELECT
    n AS id,
    FLOOR(RAND() * 100000) AS concert_id,
    n AS concert_seat_id,
    FLOOR(RAND() * 100000) AS user_id,
    'RESERVED' AS status,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 86400 + FLOOR(RAND() * 43200)) AS expired_at,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - 86400 + FLOOR(RAND() * 172800)) AS created_at,
    FROM_UNIXTIME(UNIX_TIMESTAMP(NOW()) - FLOOR(RAND() * 31536000)) AS updated_at
FROM cte;