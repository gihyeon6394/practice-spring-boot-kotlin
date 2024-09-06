# resilience4j

- reference
    - https://resilience4j.readme.io/v1.7.0/docs/getting-started
    - https://oliveyoung.tech/blog/2023-08-31/circuitbreaker-inventory-squad/
- version : 1.7.0

---

- GETTING STARTED
    - Introduction
    - Comparison to Netflix Hystrix
    - Gradle
- CORE MODULES
    - CircuitBreaker
    - Bulkhead
    - RateLimiter
    - Retry
    - TimeLimiter
    - Cache
- ADD-ON MODULES
    - Kotlin
- SPRING REACTOR
    - Getting Started
    - Examples
- SPRING BOOT 2

---

# Getting Started

## Introduction

- Resilience4j 는 경량 fault tolerance 라이브러리이다. (inspired by Netflix Hystrix)
- Java 8, functional programming 위해 설계
- Netfilx Hystrix는 Archauis에 컴파일 의존성을 가지지만, Resilience4j는 Vavr 라이브러리만을 사용 (Vavr은 다른 라이브러리에 의존성이 없음)
    - Archauis는 Guaava, Apache Commons에 의존

### Sneak preview

- CircuitBreaker와 Retry를 람다 표현식으로 구현한 예제
- 예외 발생 시 3번 retry
- retry 인터벌 설정 가능
- backoff strategy 설정 가능

```java
// default 설정으로 CircuitBreaker 생성
CircuitBreaker circuitBreaker = CircuitBreaker
        .ofDefaults("backendService");

// default 설정으로 Retry 생성
// 3번 retry 시도, 500ms 간격
Retry retry = Retry.ofDefaults("backendService");

// default 설정으로 Bulkhead 생성
Bulkhead bulkhead = Bulkhead.ofDefaults("backendService");

Supplier<String> supplier = () -> backendService
        .doSomething(param1, param2);

// circuitBreaker, retry, bulkhead를 doSomething에 적용
Supplier<String> decoratedSupplier = Decorators.ofSupplier(supplier)
        .withCircuitBreaker(circuitBreaker)
        .withBulkhead(bulkhead)
        .withRetry(retry)
        .decorate();

// supplier를 실행하고 예외로부터 복구 (람다)
String result = Try.ofSupplier(decoratedSupplier)
        .recover(throwable -> "Hello from Recovery").get();

// 람다 데코레이트 안하고 서킷 브레이커만 적용
String result = circuitBreaker.executeSupplier(backendService::doSomething);

// ThreadPoolBulkhead 에서 supplier 비동기로 실행
ThreadPoolBulkhead threadPoolBulkhead = ThreadPoolBulkhead.ofDefaults("backendService");

// CompletableFuture에 타임아웃 설정 
ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
TimeLimiter timeLimiter = TimeLimiter.of(Duration.ofSeconds(1));

CompletableFuture<String> future = Decorators.ofSupplier(supplier)
        .withThreadPoolBulkhead(threadPoolBulkhead)
        .withTimeLimiter(timeLimiter, scheduledExecutorService)
        .withCircuitBreaker(circuitBreaker)
        .withFallback(asList(TimeoutException.class,
                        CallNotPermittedException.class,
                        BulkheadFullException.class),
                throwable -> "Hello from Recovery")
        .get().toCompletableFuture();
```

## Gradle

- JDK 8 이상 필요

```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "io.github.resilience4j:resilience4j-circuitbreaker:${resilience4jVersion}"
    implementation "io.github.resilience4j:resilience4j-ratelimiter:${resilience4jVersion}"
    implementation "io.github.resilience4j:resilience4j-retry:${resilience4jVersion}"
    implementation "io.github.resilience4j:resilience4j-bulkhead:${resilience4jVersion}"
    implementation "io.github.resilience4j:resilience4j-cache:${resilience4jVersion}"
    implementation "io.github.resilience4j:resilience4j-timelimiter:${resilience4jVersion}"
}
```

# CORE MODULES

## CircuitBreaker

### Introduction

![img.png](img.png)

- CircuitBreaker는 일반적인 상태 (`CLOSED`, `OPEN`, `HALF_OPEN`), 특별한 상태 (`DISABLED`, `FORCED_OPEN`)를 가짐
- sliding window : 요청들을 저장하고 취합
    - count-based sliding window : 마지막 N개 요청 수 기반
    - time-based sliding window : 마지막 N초 요청 수 기반

### Count-based sliding window

![img_1.png](img_1.png)

- N번 측정하는 circular array (환형 배열) 사용
    - window size = 10이면, 환형 배열이 10개의 측정값을 가짐
- 점차적으로 총합을 업데이트해감
- Substract-on-Evict : 새로운 call이 들어오면 가장 오래된 call을 빼주고, (evicted) 총합 업데이트
- 공간 소모 : O(N) (N은 window size)
- 스냅샨 검색 시간 : O(1)
    - 스냅샷은 사전에 항상 업데이트되어 있음

### Time-based sliding window

- N개의 부분집계 (buckets)를 가지는 circular array 사용
    - window size = 10 (=10초)이면, 10개의 부분 집계를 가짐 (=10 buckets)
    - 각 bucket에는 특정 시간 동안의 요청 수를 가짐
- partial aggregation : 각 bucket은 특정 시간 동안의 요청 수를 가짐
- 환형 배열의 head bucket에 최근 요청을 저장
    - 뒤따르는 bucket은 이전 bucket의 요청을 저장
- 각 튜플을 개별적으로 bucket에 저장하지않고, 점진적으로 bucket과 total aggregation을 업데이트
- Substract-on-Evict :
    - 새로운 call이 들어오면 total aggregation 업데이트
    - 오래된 bucket을 제거할 때, total aggregation 업데이트
- 공간 소모 : O(N) (N은 window size)
- 스냅샨 검색 시간 : O(1)
    - 스냅샷은 사전에 항상 업데이트되어 있음
- bucket 은 3가지 정수, 1가지 long 정수로 이루어짐
    - 실패 call 수
    - slow call 수
    - total call 수
    - total duration (long)

### Failure rate and slow call rate thresholds

- `CLOSED` -> `OPEN` : 실패율이 threshold 이상일 때
    - e.g. 50% 이상 실패 시 `OPEN`
    - default로 exception은 failure로 간주
    - 실패로 간주할 예외 리스트업 가능
    - 실패, 성공이 아닌 무시로 설정할 수도 있음
- `CLOSED` -> `OPEN` : slow call rate이 threshold 이상일 때
    - e.g. 50% 이상의 call이 5초 이상 걸릴 때 `OPEN`
    - 외부 시스템이 느릴 때, `OPEN` 상태로 전환하는 예시
- failure rate와 slow call rate는 최소 콜수가 기록되면 집계하기 시작함
    - e.g. 10으로 최소 값을 지정하면, 최소 10개가 기록되면 집계 시
        - 그 이전에 9개가 모두 실패해도 `CLOSED` 상태로 유지됨
- `CallNotPermittedException` : `OPEN` 상태일 때, 호출을 거부하는 예외
- `OPEN` 상태에서 호출을 거부하면, `CallNotPermittedException` 발생
    - wait time duration이 지나 `HALF_OPEN` 상태로 전환됨
    - 지정한 숫자만큼의 call들이 서비스로 진입하게 됨
- failure rate나 slow call이 설정한 임계값 이상으로 높으면 다시 `OPEN` 상태로 전환됨
    - 임계값 미만이 되면 `CLOSED` 상태로 전환됨
- 아래 두 상태에서는 어떤 메트릭도 수집하지 않음
    - `DISABLED` 상태 : CircuitBreaker가 비활성화된 상태
    - `FORCED_OPEN` 상태 : CircuitBreaker가 강제로 `OPEN` 상태로 전환된 상태
    - 오직 직접 상태를 바꾸거나 서킷 브레이커를 리셋하면 `CLOSED` 상태로 전환됨

#### thread-safe

- CircuitBreaker는 thread-safe
    - 상태가 `AtomicReference`로 관리됨
    - 상태 전환 시 side-effect-function을 사용해 atomic하게 상태 전환
    - 동기화된 Sliding Window에 기록
- 동시에 오직 하나의 스레드만이 상태나 Sliding Window를 변경할 수 있음

![img_2.png](img_2.png)

- function call을 동기화 하지 않음
    - function call은 임계영역이 아님
    - function call을 동기화하면 심한 성능 저하를 야기함
- e.g. Sliding Window 사이즈가 15이고, CircuitBreaker가 `CLOSED` 일 떄
    - 20개의 스레드가 함수 호출을 요청하면 모든 스레드가 호출 허용 됨
- `Bulkhead`를 사용하면 동시에 호출할 수 있는 함수 호출 수를 제한할 수 있음

![img_3.png](img_3.png)

### Create a CircuitBreakerRegistry

```java
CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();
```

- in-memory 기반의 `CircuitBreakerRegistry` : CircuitBreaker 인스턴스를 생성하고 관리
- 스레드 세이프, 원자성을 보장
- `CircuitBreakerConfig` : CircuitBreaker 설정을 정의

### Create and configure a CircuitBreaker

- CircuitBreaker 설정을 커스터마이징
- builder 사용

| property                                       | default            | Description                                                                                                                                                                                                                   |
|------------------------------------------------|--------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `failureRateThreshold`                         | 50 (%)             | 실패율 임계값 (%) <br/>설정값 이상이 되면 `OPEN`                                                                                                                                                                                            |
| `slowCallRateThreshold`                        | 100 (%)            | 느린 호출 비율 임계값 (%) <br/>설정값 이상이 되면 `OPEN`                                                                                                                                                                                       |
| `slowCallDurationThreshold`                    | 60000 (ms)         | slow로 간주할 값 (ms) <br/>설정값 이상이 되면 `OPEN`                                                                                                                                                                                       |
| `permittedNumberOfCallsInHalfOpenState`        | 10                 | `HALF_OPEN` 상태에서 허용되는 호출 수                                                                                                                                                                                                    |
| `maxWaitDurationInHalfOpenState`               | 0 (ms)             | `HALF_OPEN` 상태에서 `OPEN` 전환까지 대기할 최대 시간<br/>`0` : 무한 대기                                                                                                                                                                        |
| `slidingWindowType`                            | COUNT_BASED        | 슬라이딩 윈도우 타입 <br/>`COUNT_BASED`, `TIME_BASED` 중 하나 선택                                                                                                                                                                          |
| `slidingWindowSize`                            | 100                | 슬라이딩 윈도우 크기 <br/>`COUNT_BASED`일때 마지막 N개 요청 수, `TIME_BASED` 일 때 마지막 N시간 (ms)만큼의 요청                                                                                                                                             |
| `minimumNumberOfCalls`                         | 100                | window 마다 실패율, 느린 호출 비율을 계산하기 위한 최소 호출 수 <br/>설정값 이상이 되면 계산 시작                                                                                                                                                                |
| `waitDurationInOpenState`                      | 60000 (ms)         | `OPEN` 상태에서 `HALF_OPEN` 상태로 전환할 대기 시간 (ms)                                                                                                                                                                                    |
| `automaticTransitionFromOpenToHalfOpenEnabled` | false              | `OPEN` 상태에서 `HALF_OPEN` 상태로 자동 전환 활성화 여부<br/> true : `waitDurationInOpenState` 이 지나면 `HALF_OPEN` 전환하기 위해 `waitDurationInOpenState` 동안 스레드 하나를 생성해 모니터링 <br/>false : `waitDurationInOpenState`이 지나도 요청이 들어와야 `HALF_OPEN` 으로 전환 |
| `recordExceptions`                             | empty list         | 실패로 간주할 예외 리스트                                                                                                                                                                                                                |
| `ignoreExceptions`                             | empty list         | 실패/성공으로 간주하지 않을 예외 리스트                                                                                                                                                                                                        |
| `recordFailurePredicate`                       | throwable -> true  | 예외 발생 시 실패로 간주할 커스텀 조건<br/>true를 리턴해야 실패로 간주됨                                                                                                                                                                                 |
| `ignoreExceptionsPredicate`                    | throwable -> false | 예외 발생 시 실패/성공을 평가하지 않을 커스텀 조건                                                                                                                                                                                                 |

```java
// CircuitBreaker를 커스텀하게 생성
CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        .failureRateThreshold(50) // 50% 실패 시 OPEN
        .slowCallRateThreshold(50) // 50% 느린 호출 시 OPEN
        .waitDurationInOpenState(Duration.ofMillis(1000)) // 1000ms 후 OPEN -> HALF_OPEN
        .slowCallDurationThreshold(Duration.ofSeconds(2)) // 2초 이상 호출 시 느린 호출로 간주
        .permittedNumberOfCallsInHalfOpenState(3) // HALF_OPEN 상태에서 3개의 호출 허용
        .minimumNumberOfCalls(10) // 최소 10개의 호출이 있어야 실패율, 느린 호출 비율 계산
        .slidingWindowType(SlidingWindowType.TIME_BASED) // TIME_BASED 슬라이딩 윈도우 사용
        .slidingWindowSize(5) // 5초 동안의 호출 수를 윈도우에 저장
        .recordException(e -> INTERNAL_SERVER_ERROR
                .equals(getResponse().getStatus())) // 500 에러 발생 시 실패로 간주
        .recordExceptions(IOException.class, TimeoutException.class) // IOException, TimeoutException 발생 시 실패로 간주
        .ignoreExceptions(BusinessException.class, OtherBusinessException.class) // BusinessException, OtherBusinessException은 실패로 간주하지 않음
        .build();

// CircuitBreakerRegistry에 CircuitBreakerConfig를 사용해 CircuitBreaker 생성
CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(circuitBreakerConfig);


// registry에서 CircuitBreaker를 이름 name1으로 생성
CircuitBreaker circuitBreakerWithDefaultConfig = circuitBreakerRegistry.circuitBreaker("name1");

// registry에서 circuitBreakerConfig를 사용해 CircuitBreaker를 이름 name2로 생성
CircuitBreaker circuitBreakerWithCustomConfig = circuitBreakerRegistry.circuitBreaker("name2", circuitBreakerConfig);

```

```java
CircuitBreakerConfig defaultConfig = circuitBreakerRegistry
        .getDefaultConfig();

// 기존 컨피그를 사용해 새로운 컨피그 생성
CircuitBreakerConfig overwrittenConfig = CircuitBreakerConfig
        .from(defaultConfig)
        .waitDurationInOpenState(Duration.ofSeconds(20))
        .build();
```

```java
// registry없이 바로 CircuitBreaker 생성
CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
        .recordExceptions(IOException.class, TimeoutException.class)
        .ignoreExceptions(BusinessException.class, OtherBusinessException.class)
        .build();

CircuitBreaker customCircuitBreaker = CircuitBreaker.of("testName", circuitBreakerConfig);
```

```java
Map<String, String> circuitBreakerTags = Map.of("key1", "value1", "key2", "value2");

// builder로 CircuitBreaker 생성
CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.custom()
        .withCircuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
        .addRegistryEventConsumer(new RegistryEventConsumer() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent entryAddedEvent) {
                // implementation
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent entryRemoveEvent) {
                // implementation
            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent entryReplacedEvent) {
                // implementation
            }
        })
        .withTags(circuitBreakerTags)
        .build();

CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("testName");
```

```java
// 별도의 registry 구현체 주입해서 CircuitBreakerRegistry 생성
CircuitBreakerRegistry registry = CircuitBreakerRegistry.custom()
        .withRegistryStore(new YourRegistryStoreImplementation())
        .withCircuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
        .build();
```

### Decorate and execute a functional interface

- CircuitBreaker 사용시 `Callable`, `Supplier`, `Runnable` 등의 함수형 인터페이스를 데코레이트해서 사용 가능
- `Try.of(...)`, `Try.run(...)` 등을 사용해 람다 표현식을 데코레이트
- `COSED`, `HALF_OPEN` 상ㅇ태일 때만 호출됨

```java
// Given : testName의 CircuitBreaker 생성
CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");


// When : function 호출을 데코레이트
CheckedFunction0<String> decoratedSupplier = CircuitBreaker
        .decorateCheckedSupplier(circuitBreaker, () -> "This can be any method which returns: 'Hello'");

// chaining
Try<String> result = Try.of(decoratedSupplier)
        .map(value -> value + " world'");

// Then : 호출 결과 확인
assertThat(result.isSuccess()).isTrue();
assertThat(result.get()).isEqualTo("This can be any method which returns: 'Hello world'");

```

### Consume emitted RegistryEvents

- event consumer를 등록해서 CircuitBreaker의 생성, 교체, 삭제 이벤트 감지

```java
CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.ofDefaults();

circuitBreakerRegistry.getEventPublisher()
  .onEntryAdded(entryAddedEvent -> {
    CircuitBreaker addedCircuitBreaker = entryAddedEvent.getAddedEntry();
    LOG.info("CircuitBreaker {} added", addedCircuitBreaker.getName());
  })
  .onEntryRemoved(entryRemovedEvent -> {
    CircuitBreaker removedCircuitBreaker = entryRemovedEvent.getRemovedEntry();
    LOG.info("CircuitBreaker {} removed", removedCircuitBreaker.getName());
  });
```

### Consume emitted CircuitBreakerEvents

- `CircuitBreakerEvent` : state 변화, 리셋, 성공 call, 에러 콜 저장, 에러 무시

```java
circuitBreaker.getEventPublisher()
    .onSuccess(event -> logger.info(...))
    .onError(event -> logger.info(...))
    .onIgnoredError(event -> logger.info(...))
    .onReset(event -> logger.info(...))
    .onStateTransition(event -> logger.info(...));

circuitBreaker.getEventPublisher()
    .onEvent(event -> logger.info(...));
```

```java
// beffured events
CircularEventConsumer<CircuitBreakerEvent> ringBuffer = new CircularEventConsumer<>(10);

circuitBreaker.getEventPublisher().onEvent(ringBuffer);
List<CircuitBreakerEvent> bufferedEvents = ringBuffer.getBufferedEvents()
```

### Override the RegistryStore

- in-memory `RegistryStore`를 오버라이드해서 커스텀 구현체
- `CacheCircuitBreakerRegistryStore` : 일정 기간동안 사용하지 않은 인스턴스를 제거하는 `RegistryStore`

```java
// CacheCircuitBreakerRegistryStore를 주입하여 사용
CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.custom()
  .withRegistryStore(new CacheCircuitBreakerRegistryStore())
  .build();

```
