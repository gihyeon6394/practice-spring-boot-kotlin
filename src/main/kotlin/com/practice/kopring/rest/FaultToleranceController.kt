package com.practice.kopring.rest

import com.mysql.cj.jdbc.exceptions.MySQLTimeoutException
import com.practice.kopring.application.tmp.Person
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author gihyeon-kim
 *
 * fault tolerant 한 rest api를 설계해보자
 */
@RestController
@RequestMapping("/fault-tolerance")
class FaultToleranceController {

    /**
     * DB 타임아웃이 발생하면 대체 데이터를 반환하는 api
     */
    @GetMapping("/persons")
    @CircuitBreaker(name = "circuitBreaker-fault-tolerance", fallbackMethod = "fallbackJustMockData")
    fun getPerson(): List<Person> {
        throw MySQLTimeoutException()
        return listOf(Person("Kim", 20), Person("Lee", 19), Person("Park", 21))
    }

    private fun fallbackJustMockData(e: MySQLTimeoutException): List<Person> {
        LoggerFactory.getLogger(javaClass).error("CircuitBreaker is open", e)
        return listOf(Person("unknown data", 0))
    }

}
