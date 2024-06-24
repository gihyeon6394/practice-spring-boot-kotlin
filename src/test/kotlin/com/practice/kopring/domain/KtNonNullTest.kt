package com.practice.kopring.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class KtNonNullTest {

    @Test
    @DisplayName("kotlin non-null 필드에 런타임에 null 값을 할당할 수 있다.")
    fun test() {
        val jsonPerson = """
            {
                "carList": [null]
            }   
                """.trimIndent()

        // jackson 라이브러리는 null 값을 할당할 수 있다.
        val person = ObjectMapper()
            .registerKotlinModule()
            .readValue(jsonPerson, Person::class.java)

        assert(person.carList != null)
        assert(person.carList[0] == null)
    }


    @Test
    @DisplayName("kotlin non-null 필드에 런타임에 null 값을 할당할 수 있다. + NPE 발생")
    fun testWithNPE() {
        val jsonPerson = """
            {
                "carList": [null]
            }   
                """.trimIndent()

        val person = ObjectMapper()
            .registerKotlinModule()
            .readValue(jsonPerson, Person::class.java)

        // NPE 발생
        assertThrows<NullPointerException> {
            person.carList[0].name
        }
    }
}

data class Person(
     val carList: List<Car> = emptyList(),
)

data class Car(
    val name: String,
)