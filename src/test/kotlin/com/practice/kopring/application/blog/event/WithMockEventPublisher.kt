package com.practice.kopring.application.blog.event

import io.mockk.mockk
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary

/**
 * @author gihyeon-kim
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(MockEventPublisherConfig::class)
annotation class WithMockEventPublisher

@TestConfiguration
class MockEventPublisherConfig {

    @Bean
    @Primary
    fun eventPublisher(): ApplicationEventPublisher = mockk(relaxed = true)
}
