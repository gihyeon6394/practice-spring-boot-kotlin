package com.practice.kopring.resilience4j

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.practice.kopring.application.tmp.Person
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post


/**
 * @author gihyeon-kim
 */
@SpringBootTest
@AutoConfigureMockMvc
class FaultToleranceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    @DisplayName("CircuitBreaker를 통해 DB 타임아웃 발생 시 대체 데이터를 반환한다.")
    fun testCircleBreaker() {
        mockMvc.get("/fault-tolerance/persons") {
        }.andExpect {
            status { isOk() }
            content {
                listOf(Person("Kim", 20), Person("Lee", 19), Person("Park", 21)).also {
                    ObjectMapper().registerKotlinModule().writeValueAsString(it)
                }
            }
        }.andReturn()
    }


    @Test
    @DisplayName("CircuitBreaker를 통해 비즈니스 로직에서 예외 발생 시 대체 상태를 반환한다.")
    fun testCircleBreakerLogic() {
        mockMvc.post("/fault-tolerance/some-logic") {
        }.andExpect {
            status { isOk() }
            content {
                0
            }
        }.andReturn()
    }
}
