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
        // some logic ...
        return 0
    }
}
