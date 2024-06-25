package com.practice.kopring.domain

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.InvalidNullException
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
        val jsonTmp = """
            {
                "list": [null]
            }   
                """.trimIndent()

        val person = ObjectMapper()
            .registerKotlinModule()
            .readValue(jsonTmp, Tmp1::class.java)

        assert(person.list != null)
        assert(person.list[0] == null)

        assertThrows<NullPointerException> {
            person.list[0].name
        }
    }

    @Test
    @DisplayName("kotlin non-null 필드에 런타임에 null 값을 할당할 수 있지만, @JsonSetter로 null을 다룰 수 있다")
    fun testWithJsonSetter() {
        var jsonTmp2 = """
            {
                "childList1": [null, { "name": "BMW" }]
            }   
                """.trimIndent()

        val tmp21 = ObjectMapper()
            .registerKotlinModule()
            .readValue(jsonTmp2, Tmp2::class.java)

        assert(tmp21.childList1.size == 1)
        assert(tmp21.childList1[0].name == "BMW")

        jsonTmp2 = """
            {
                "childList2": [null, { "name": "BMW" }]
            }   
                """.trimIndent()


        assertThrows<InvalidNullException> {
            ObjectMapper()
                .registerKotlinModule()
                .readValue(jsonTmp2, Tmp2::class.java)

        }
    }
}

data class Tmp2(
    @JsonSetter(contentNulls = Nulls.SKIP) // null인 element는 무시
    val childList1: List<Child> = emptyList(),

    @JsonSetter(contentNulls = Nulls.FAIL) // null인 element가 있으면 Exception을 발생
    val childList2: List<Child> = emptyList(),
)

data class Tmp1(
    val list: List<Child> = emptyList(),
)

data class Child(
    val name: String,
)




