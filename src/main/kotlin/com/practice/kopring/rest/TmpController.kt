package com.practice.kopring.rest

import com.mysql.cj.jdbc.exceptions.MySQLTimeoutException
import com.practice.kopring.application.feign.TmpFeign
import com.practice.kopring.application.member.repo.MemberRepo
import com.practice.kopring.application.tmp.ParentBean
import com.practice.kopring.application.tmp.Person
import com.practice.kopring.rest.model.Car
import com.practice.kopring.rest.model.CreatePersonRequest
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * @author gihyeon-kim
 */
@RestController
class TmpController(
    private val beans: List<ParentBean>,
    private val tmpFeign: TmpFeign,
    private val memberRepo: MemberRepo,
    private val eventPublisher: ApplicationEventPublisher,
    ) {
    private val log = LoggerFactory.getLogger(javaClass)

    @GetMapping("/test")
    fun test(): String {
        tmpFeign.testReadTimeout()
        return "Hello, World!"
    }

    @GetMapping("/test2")
    fun test2(): List<Person> {
        return listOf(Person("Kim", 20), Person("Lee", 19), Person("Park", 21));
    }

    @GetMapping("/beanList")
    fun beanList(): List<String> {
        return beans.map { it.javaClass.simpleName }
    }

    @PostMapping("/createPerson", consumes = ["application/json"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun createPerson(
        @RequestBody createPersonRequest: CreatePersonRequest,
    ): HttpStatus {
        // something happens ...
        return HttpStatus.NO_CONTENT
    }


    @GetMapping("/testReadTimeout")
    fun testReadTimeout(): String {
        Thread.sleep(3000)
        return "success msg"
    }

    @GetMapping("/members")
    fun members(): List<String> {
        return memberRepo.findAll().map { it.name }
    }

    @GetMapping("/test-circuitBreaker")
    @CircuitBreaker(name = "testCircuitBreaker", fallbackMethod = "fallback")
    fun testCircuitBreaker(): List<String> {
        log.info("testCircuitBreaker")
        throw MySQLTimeoutException("Timeout")
        return memberRepo.findAll().map { it.name }
    }

    private fun fallback(e: MySQLTimeoutException): List<String> {
        LoggerFactory.getLogger(javaClass).error("Handled the exception when the CircuitBreaker is open", e)
        return listOf("서비스가 일시적으로 불안정합니다. 잠시 후 다시 시도해주세요.")
    }

    @PostMapping("/ignoreTest", consumes = ["application/json"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun ignoreTest(
        @RequestBody car: Car,
    ): HttpStatus {
        // something happens ...
        return HttpStatus.NO_CONTENT
    }

    @GetMapping("/eventTest")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun eventTest(): HttpStatus {
        // something happens ...
        eventPublisher.publishEvent(MemberSignupEvent("Kim"))
        return HttpStatus.NO_CONTENT
    }
}

data class MemberSignupEvent(val name: String)
