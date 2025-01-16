# Filter & Interceptor

시스템 성격에 적합하게 Filter, Interceptor 를 활용해 기능의 관점을 분리하여 개선하는 과정을 기록합니다.

## Filter

- Filter는 Spring Context 외부에 존재하기 때문에 Spring과 무관하게 전역적으로 처리해야 하는 작업을 수행하는 것이 적절합니다.
- 따라서 각 요청에 대한 로그를 기록하는 로직을 Filter로
  구현하였습니다. ([LoggingFilter.kt](https://github.com/psh10066/hhplus-server-concert/blob/553746883c2b8c68c254a1f99c645d044670a1e6/src/main/kotlin/kr/hhplus/be/server/support/filter/LoggingFilter.kt))
    - Spring Context 외부에 존재하기 때문에 API 호출 시 예외 발생 여부와 상관없이 동작의 종료 이후 로깅이 수행됩니다.

## Interceptor

- Interceptor는 Spring Context 내부에 존재하기 때문에 Spring Bean을 로드하거나 컨트롤러로 넘겨주기 위한 정보를 가공하기에 용이합니다.
- 따라서 기존에 MethodArgumentResolver에서 수행하던 userId 및 token의 검증 절차를 Interceptor로 이동하여 관리하도록
  구성하였습니다. ([QueueInterceptor.kt](https://github.com/psh10066/hhplus-server-concert/blob/7eb546d501e11736deed631ab47b2f59e7c90dfe/src/main/kotlin/kr/hhplus/be/server/support/interceptor/QueueInterceptor.kt), [UserInterceptor.kt](https://github.com/psh10066/hhplus-server-concert/blob/7eb546d501e11736deed631ab47b2f59e7c90dfe/src/main/kotlin/kr/hhplus/be/server/support/interceptor/UserInterceptor.kt))
- 기존의 MethodArgumentResolver는 Interceptor에서 조회한 User 혹은 Queue 객체를 ServletRequest에서 꺼내어 Method Argument로 주입해주는 역할을 수행합니다.
