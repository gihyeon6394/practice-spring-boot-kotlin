package com.practice.kopring.rest

import com.practice.kopring.PracticeKopringApplication
import com.practice.kopring.application.tmp.Person
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * @author gihyeon-kim
 */
@RestController
class TestController {

    private val log = LoggerFactory.getLogger(PracticeKopringApplication::class.java)

    @GetMapping("/test")
    fun test(): String {

        log.trace("Hello, World!")
        log.debug("Hello, World!")
        log.info("Hello, World!")
        log.warn("Hello, World!")
        log.error("Hello, World!")

        return "Hello, World!"
    }

    @GetMapping("/test2")
    fun test2(): List<Person> {
        return listOf(Person("Kim", 20), Person("Lee", 19), Person("Park", 21));
    }
}
