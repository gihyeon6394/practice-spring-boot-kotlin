package com.practice.kopring.application.blog.event

import io.mockk.clearMocks
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.event.ApplicationEvents
import org.springframework.test.context.event.RecordApplicationEvents

/**
 * @see https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/application-events.html
 *
 * @author gihyeon-kim
 */
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RecordApplicationEvents
class JoinEventTestSolutionWithoutMock(
    private val joinService: JoinService,
) {
    @Autowired
    private lateinit var events: ApplicationEvents

    @BeforeEach
    fun setUp() {
        // ApplicationEvents 싱글톤 목이므로 테스트마다 초기화해 이벤트를 정확히 검증하도록 함
        clearMocks(events)
    }

    @Test
    @DisplayName("회원가입시 이벤트를 발행")
    fun joinTest() = runTest {
        joinService.join(
            name = "James",
            email = "test@test.com"
        )

        val events = events.stream(JoinEvent::class.java).toList()
        assert(events.size == 1)
        with(events.first()) {
            assert(name == "James")
            assert(email == "test@test.com")
        }
    }
}
