import http from 'k6/http';
import {check, sleep} from 'k6';

const MAX_VUS = 10;

export const options = {
    scenarios: {
        concert_end_to_end: {
            executor: 'per-vu-iterations',
            vus: MAX_VUS,
            iterations: 30,
        }
    }
};

const BASE_URL = 'http://localhost:8080';

// -------------------------------------------------------------------------------------
// 1) 대기열 토큰 발급 API
// -------------------------------------------------------------------------------------
function issueQueueToken(userId) {
    const url = `${BASE_URL}/api/v1/queues/token`;
    const params = {
        headers: {
            'Content-Type': 'application/json',
            'userId': String(userId),
        },
        tags: {
            name: '1_대기열_토큰_발급_API',
        }
    };
    const res = http.post(url, null, params); // 바디 없이 POST

    check(res, {
        'issueQueueToken: status was 200': (r) => r.status === 200,
    });

    const json = JSON.parse(res.body);
    return json.data.token;
}

// -------------------------------------------------------------------------------------
// 2) 대기열 토큰 대기 순위 조회 API
//    - 1초 간격 폴링하여 ACTIVE 될 때까지 반복 요청
// -------------------------------------------------------------------------------------
function getQueueRank(token, userId) {
    const url = `${BASE_URL}/api/v1/queues/token/rank`;
    const params = {
        headers: {
            token: token,
            userId: String(userId),
        },
        tags: {
            name: '2_대기열_토큰_대기_순위_조회_API',
        }
    };
    const res = http.get(url, params);

    check(res, {
        'getQueueRank: status was 200': (r) => r.status === 200,
    });

    const json = JSON.parse(res.body);
    return json.data;
}

// -------------------------------------------------------------------------------------
// 3) 인기 콘서트 TOP 20 조회 API
// -------------------------------------------------------------------------------------
function getPopularConcerts(token) {
    const url = `${BASE_URL}/api/v1/reservations/concert/popular`;
    const params = {
        headers: {
            token: token,
        },
        tags: {
            name: '3_인기_콘서트_TOP_20_조회_API',
        }
    };
    const res = http.get(url, params);

    check(res, {
        'getPopularConcerts: status was 200': (r) => r.status === 200,
    });

    return res;
}

// -------------------------------------------------------------------------------------
// 4) 콘서트 목록 조회 API
// -------------------------------------------------------------------------------------
function getConcerts(token) {
    const url = `${BASE_URL}/api/v1/concerts?page=1&size=20`;
    const params = {
        headers: {
            token: token,
        },
        tags: {
            name: '4_콘서트_목록_조회_API',
        }
    };
    const res = http.get(url, params);

    check(res, {
        'getConcerts: status was 200': (r) => r.status === 200,
    });

    return res;
}

// -------------------------------------------------------------------------------------
// 5) 콘서트 일정 조회 API
// -------------------------------------------------------------------------------------
function getConcertSchedules(token, concertId) {
    const url = `${BASE_URL}/api/v1/concerts/${concertId}/schedules`;
    const params = {
        headers: {
            token: token,
        },
        tags: {
            name: '5_콘서트_일정_조회_API',
        },
    };
    const res = http.get(url, params);

    check(res, {
        'getConcertSchedules: status was 200': (r) => r.status === 200,
    });

    const json = JSON.parse(res.body);
    return json.data.schedules;
}

// -------------------------------------------------------------------------------------
// 6) 콘서트 좌석 조회 API
// -------------------------------------------------------------------------------------
function getConcertSeats(token, concertScheduleId) {
    const url = `${BASE_URL}/api/v1/concerts/schedules/${concertScheduleId}/seats`;
    const params = {
        headers: {
            token: token,
        },
        tags: {
            name: '6_콘서트_좌석_조회_API',
        },
    };
    const res = http.get(url, params);

    check(res, {
        'getConcertSeats: status was 200': (r) => r.status === 200,
    });

    const json = JSON.parse(res.body);
    return json.data.seats;
}

// -------------------------------------------------------------------------------------
// 7) 콘서트 예약(좌석 선점) API
// -------------------------------------------------------------------------------------
function reserveConcertSeat(token, concertSeatId) {
    const url = `${BASE_URL}/api/v1/reservations/concert`;
    const payload = JSON.stringify({ concertSeatId: concertSeatId });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            token: token,
        },
        tags: {
            name: '7_콘서트_예약_API',
        },
    };
    const res = http.post(url, payload, params);

    check(res, {
        'reserveConcertSeat: status was 200': (r) => r.status === 200,
    });

    return res;
}

// -------------------------------------------------------------------------------------
// 8) 잔액 조회 API
// -------------------------------------------------------------------------------------
function getUserWalletBalance(userId) {
    const url = `${BASE_URL}/api/v1/user-wallets/balance`;
    const params = {
        headers: {
            userId: String(userId),
        },
        tags: {
            name: '8_잔액_조회_API',
        },
    };
    const res = http.get(url, params);

    check(res, {
        'getUserWalletBalance: status was 200': (r) => r.status === 200,
    });

    return res;
}

// -------------------------------------------------------------------------------------
// 9) 잔액 충전 API
// -------------------------------------------------------------------------------------
function chargeUserWalletBalance(userId, amount) {
    const url = `${BASE_URL}/api/v1/user-wallets/balance`;
    const payload = JSON.stringify({ amount: amount });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            userId: String(userId),
        },
        tags: {
            name: '9_잔액_충전_API',
        },
    };
    const res = http.patch(url, payload, params);

    check(res, {
        'chargeUserWalletBalance: status was 200': (r) => r.status === 200,
    });

    return res;
}

// -------------------------------------------------------------------------------------
// 10) 콘서트 결제 API
// -------------------------------------------------------------------------------------
function payForConcert(token, reservationId) {
    const url = `${BASE_URL}/api/v1/reservations/concert/payment`;
    const payload = JSON.stringify({ reservationId: reservationId });
    const params = {
        headers: {
            'Content-Type': 'application/json',
            token: token,
        },
        tags: {
            name: '10_콘서트_결제_API',
        }
    };
    const res = http.post(url, payload, params);

    check(res, {
        'payForConcert: status was 200': (r) => r.status === 200,
    });

    return res;
}

// -------------------------------------------------------------------------------------
// 시나리오 실행
// -------------------------------------------------------------------------------------
export default function () {
    const userId = MAX_VUS * __ITER + __VU; // 요청 당 1명 고정
    const concertId = Math.floor(__ITER / 3) + 1; // 콘서트 ID는 3번 반복까지 동일
    const concertScheduleId = __ITER + 1; // 콘서트 일정 ID는 반복 당 하나
    const concertSeatId = userId; // 좌석 ID는 요청 당 하나

    console.log(`userId: ${userId}, concertId: ${concertId}, concertScheduleId: ${concertScheduleId}`);

    // 1) 대기열 토큰 발급 API
    const token = issueQueueToken(userId);

    // (2) Polling: 대기열 토큰 활성화될 때까지 1초마다 재조회
    while (true) {
        const { status } = getQueueRank(token, userId);
        if (status === 'ACTIVE') {
            break;
        }
        sleep(1); // 1초 후 재시도
    }

    // (3) 인기 콘서트 조회
    getPopularConcerts(token);

    // (4) 콘서트 목록 조회
    getConcerts(token);

    // (5) 콘서트 일정 조회
    const schedules = getConcertSchedules(token, concertId);

    // (6) 콘서트 좌석 조회
    let seats = getConcertSeats(token, concertScheduleId);

    // (7) 좌석 선점 로직
    //     좌석이 없거나 이미 예약됐다면 재시도 (예시 로직)
    let reservationId = null;
    while (seats.length > 0) {
        const seatRes = reserveConcertSeat(token, concertSeatId);

        if (seatRes.status === 200) {
            console.log(`유저${userId} 좌석${concertSeatId} 선점 성공`)
            // 예약 성공
            const json = JSON.parse(seatRes.body);
            reservationId = json.data?.reservationId;
            break;
        } else {
            console.log(`유저${userId} 좌석${concertSeatId} 선점 실패`)

            // 이미 예약된 좌석이거나 오류 발생 시, 다시 좌석 목록 조회
            seats = getConcertSeats(token, concertScheduleId);

            if (seats.length === 0) {
                console.log('No more available seats. Flow ends here.');
                return;
            }
        }
    }

    // 예약 못했으면 종료
    if (!reservationId) {
        console.log('Failed to reserve seat. Flow ends.');
        return;
    }

    // (8) 지갑 잔액 조회
    getUserWalletBalance(userId);

    // (9) 잔액 충전
    chargeUserWalletBalance(userId, 1000000);

    // (10) 결제
    payForConcert(token, reservationId);
}