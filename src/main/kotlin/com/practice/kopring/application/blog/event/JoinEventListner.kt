package com.practice.kopring.application.blog.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

/**
 * @author gihyeon-kim
 */
@Component
class MailSendListener {
    @EventListener
    fun handleJoinEvent(event: JoinEvent) {
        println("Received join event: $event")
        // send a welcome email...
    }
}

@Component
class SendTopicListener {
    @EventListener
    fun handleJoinEvent(event: JoinEvent) {
        println("Received join event for topic: $event")
        // write join event to a topic...
    }
}
