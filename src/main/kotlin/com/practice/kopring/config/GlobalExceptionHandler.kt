package com.practice.kopring.config

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * @author gihyeon-kim
 *
 * UncaughtException 발생시 slf4j logger를 통해 로그를 남김
 */
@Component
class GlobalExceptionHandler : Thread.UncaughtExceptionHandler {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread?, e: Throwable?) {
        log.error("this is my error log : $e")
    }
}
