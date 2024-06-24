package com.practice.kopring.rest

import com.practice.kopring.PracticeKopringApplication
import com.practice.kopring.application.tmp.ParentBean
import com.practice.kopring.application.tmp.Person
import com.practice.kopring.rest.model.CreatePersonRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * @author gihyeon-kim
 */
@RestController
class TmpController(
    private val beans: List<ParentBean>,
) {
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
}
