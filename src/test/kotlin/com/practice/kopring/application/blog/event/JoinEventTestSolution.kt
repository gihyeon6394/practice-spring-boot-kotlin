package com.practice.kopring.application.blog.event

import io.mockk.Runs
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationEventPublisher
import org.springframework.test.context.TestConstructor

/**
 * @author gihyeon-kim
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WithMockEventPublisher
class JoinEventTestSolution (
    private val  joinService: JoinService,
    private val eventPublisher: ApplicationEventPublisher,
){
    @Test
    @DisplayName("회원가입시 이벤트를 발행")
    fun joinTest() = runTest{
        every {
            eventPublisher.publishEvent(any<JoinEvent>())
        } just Runs

        joinService.join(
            name = "James",
            email = "test@test.com"
        )

        coVerify(exactly = 1) {
            eventPublisher.publishEvent(any<JoinEvent>())
        }
    }
}
