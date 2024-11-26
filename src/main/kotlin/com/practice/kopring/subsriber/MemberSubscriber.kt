package com.practice.kopring.subsriber

import com.practice.kopring.rest.MemberSignupEvent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

/**
 * @author gihyeon-kim
 */
@Configuration
class MemberSubscriber {
    private val log = LoggerFactory.getLogger(javaClass)

    @EventListener
    suspend fun memberSignup(event: MemberSignupEvent) {
        log.info("Member Signup Event: $event")

        CoroutineScope(Default).launch { // 문제점 : 여기서 발생한 예외는 uncaught exceptions로 간주 spring logger가 모름
            delay(1000)
            if (event.name == "Kim") {
                throw RuntimeException("Kim is not allowed to sign up")
            }
        }

    }

    val handler = CoroutineExceptionHandler { _, exception ->
        log.error("An error occurred zzzzzz : $exception")
    }
}

