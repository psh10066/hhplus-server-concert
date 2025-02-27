# 성능 테스트 절차

1. 아래 명령어 실행 (오류가 발생하는 경우 data 폴더 삭제 후 실행)
    ```shell
    docker-compose -f docker-compose.load-test.yml up -d
    ```
    - 참고) 최초 1회 Spring Application 빌드 시 오랜 시간 소요될 수 있음

2. http://localhost:3000 Grafana 접속
    - 초기 계정: admin / admin

3. k6 결과 확인용 InfluxDB DataSource 추가
    ```text
    [HTTP]
    - URL: http://influxdb:8086
        
    [InfluxDB Details]
    - Database: influxdb
    - User: influxdb
    - Password: influxdb
    ```

4. 모니터링용 Prometheus DataSource 추가
    ```text
    [Connection]
    - Prometheus server URL: http://prometheus:9090
    ```

5. 모니터링용 Grafana Dashboard 추가
    ```text
    [Dashboard ID]
    - K6 Load Testing Results: 10660   
    - Spring Boot Monitoring: 11378
    - MySQL Exporter: 7362
    - Redis Exporter: 763
    - Kafka Exporter: 7589
    ```
   
6. 테스트 데이터 추가
   - `archive/insert-load-test-data.sql` 실행

7. k6 부하 테스트 실행
    ```shell
    k6 run \
      --out influxdb=http://localhost:8086/influxdb \
      k6/e2e.js
    ```

8. 부하 테스트 결과 확인