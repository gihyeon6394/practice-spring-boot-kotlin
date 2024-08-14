package com.practice.kopring.jackson

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

/**
 * @author gihyeon-kim
 */

@SpringBootTest
class JsonPropertyTest {

    @Test
    @DisplayName("@JsonIgnore 선언한 프로퍼티는 직렬화시 제외된다.")
    fun test1() {
        data class Person(
            val name: String,
            @JsonIgnore
            val age: Int? = null,
        )

        val person = Person("홍길동", 27)

        person
            .run {
                ObjectMapper().registerKotlinModule().writeValueAsString(this).let { jsonStr ->
                    assert(jsonStr.contains("age").not())
                }
            }

        val personJson = """
            {
                "name": "홍길동",
                "age": 27
            }
        """.trimIndent()

        ObjectMapper().registerKotlinModule().readValue(personJson, Person::class.java)
            .run {
                assert(this.age == null)
            }
    }

    @Test
    @DisplayName("직렬화 가능, 역직렬화 시 제외")
    fun test2() {
        data class Person(
            val name: String,
            @JsonProperty(access = JsonProperty.Access.READ_ONLY)
            val age: Int? = null,
        )

        val person = Person("홍길동", 27)

        person.run {
            ObjectMapper().registerKotlinModule().writeValueAsString(this).let { jsonStr ->
                println(jsonStr)
                assert(jsonStr.contains("age"))
            }
        }
    }

    @Test
    @DisplayName("직렬화 불가능, 역직렬화 가능")
    fun test3() {
        data class Person(
            val name: String,
            @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
            var age: Int? = null,
        )

        val person = Person("홍길동", 27)

        ObjectMapper().registerKotlinModule().writeValueAsString(person)
            .run {
                assert(this.contains("age").not())
            }

        val json = """
            {
                "name": "홍길동",
                "age": 27
            }
        """.trimIndent()

        ObjectMapper().registerKotlinModule().readValue(json, Person::class.java)
            .run {
                assert(this.age == 27)
            }
    }
}
