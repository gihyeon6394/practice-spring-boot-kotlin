package com.practice.kopring.application.blog

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author gihyeon-kim
 */
@SpringBootTest
class EnumSerializeTest {
    @Test
    @DisplayName("json 을 enum 으로 역직렬화하는 것은 sensitive")
    fun test() {
        """
            {"company" : "NIKON"}   
        """.trimIndent()
            .apply {
                ObjectMapper()
                    .registerKotlinModule()
                    .readValue(this, Camera::class.java)
                    .also { assert(it.company == Company.NIKON) }
            }
        """
            {"company" : "nikon"}   
        """.trimIndent()
            .apply {
                assertThrows<com.fasterxml.jackson.databind.exc.InvalidFormatException> {
                    ObjectMapper()
                        .registerKotlinModule()
                        .readValue(this, Camera::class.java)
                }
            }
    }

    private data class Camera(
        val company: Company,
    )

    private enum class Company {
        NIKON,
        CANON,
        SONY,
        UNKNOWN,
        ;
    }
}





