package com.practice.kopring.presentation

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

/**
 * @author gihyeon-kim
 */
@SpringBootTest
@AutoConfigureMockMvc
class TmpControllerTest(

) {
    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun createPerson() {
        mockMvc.post("/createPerson") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                    {
                        "name": "Kim",
                        "age": 20,
                        "carList": [ { "name": "BMW", "color": "blue" }, { "name": "Benz", "color": "black" }]
                    }
                    """.trimIndent()
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun createPersonBadRequest() {
        mockMvc.post("/createPerson") {
            contentType = MediaType.APPLICATION_JSON
            content = """
                    {
                        "name": "Kim",
                        "age": 20,
                        "carList": [ { "name": "BMW", "color": "blue" }, { "name": "Benz", "color": "black" }, null]
                    }
                    """.trimIndent()
            accept = MediaType.APPLICATION_JSON
        }.andExpect {
            status { isBadRequest() }
        }
    }

}
