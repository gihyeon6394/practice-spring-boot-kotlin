package com.practice.kopring.application.blog.event

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

/**
 * @author gihyeon-kim
 */
@Service
class JoinService(
    private val eventPublisher: ApplicationEventPublisher,
) {

    suspend fun join(
        name: String,
        email: String,
    ) {
        // some business logic... (e.g., save user to the database)

        // event!
        eventPublisher.publishEvent(JoinEvent(name, email))
    }
}

data class JoinEvent(val name: String, val email: String)
