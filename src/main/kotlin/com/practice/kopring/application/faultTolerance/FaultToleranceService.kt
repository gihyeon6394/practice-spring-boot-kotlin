package com.practice.kopring.application.faultTolerance

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Service

/**
 * @author gihyeon-kim
 */
@Service
class FaultToleranceService {

    @CircuitBreaker(name = "circuitBreaker-fault-tolerance", fallbackMethod = "fallbackLogic")
    fun someLogicWithExternalAPI(): Int {
        callExternalAPI()
        // some logic ...
        return 100
    }

    fun callExternalAPI() {
        throw RuntimeException("External API is not available")
    }

    private fun fallbackLogic(e: RuntimeException): Int {
        println("fallback logic is called")
        // some fallback logic ...
        return 0
    }

    /**
     * IllegalArgumentException을 발생시키는 메서드
     * IllegalArgumentException는 서킷브레이커 대상이 아니므로 fallback 로직이 호출되지 않는다.
     */
    @CircuitBreaker(name = "circuitBreaker-fault-tolerance", fallbackMethod = "fallbackLogicOpened")
    fun someLoginWithArgument() {
        throw IllegalArgumentException("Illegal Argument")
    }

    fun fallbackLogicOpened(e: RuntimeException) {
        println("fallback logic is called")
        // some fallback logic ...
    }
}
