package com.practice.kopring.application.blog

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

/**
 * @author gihyeon-kim
 */
@SpringBootTest
class JsonCreatorSolutionTest {
    @Test
    @DisplayName("enum을 json (역)직렬화 시 JsonCreator 를 사용하여 처리할 수 있다.")
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
                ObjectMapper()
                    .registerKotlinModule()
                    .readValue(this, Camera::class.java)
                    .also { assert(it.company == Company.NIKON) }
            }

        """
            {"company" : "i don't know"}
        """.trimIndent()
            .apply {
                ObjectMapper()
                    .registerKotlinModule()
                    .readValue(this, Camera::class.java)
                    .also { assert(it.company == Company.UNKNOWN) }
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

        companion object {
            @JsonCreator
            @JvmStatic
            fun from(carType: String) =
                entries.firstOrNull { it.name == carType.uppercase(Locale.getDefault()) } ?: UNKNOWN
        }
    }
}
