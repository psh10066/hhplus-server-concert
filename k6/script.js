import http from 'k6/http';

export const options = {
    scenarios: {
        getConcert: {
            executor: 'constant-arrival-rate',
            rate: 2000, // 초당 10,000 요청
            timeUnit: '1s', // 초당 요청 기준
            duration: '1m', // 1분 동안 유지
            preAllocatedVUs: 5000, // 최소 VUs (가용한 만큼)
            maxVUs: 10000, // 최대 VUs
        }
    }
};

export default function () {
    const url = 'http://localhost:8080/api/v1/reservations/concert/popular';

    const params = {
        headers: {
            'Content-Type': 'application/json',
            'token': '73779fe4-3a27-4619-9f9c-0d4b59ce9048|ACTIVE|2025-02-06T23:29:14',
        },
    };

    http.get(url, params);
}
