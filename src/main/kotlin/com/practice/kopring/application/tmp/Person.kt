package com.practice.kopring.application.tmp

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * @author gihyeon-kim
 */
data class Person(
    val name: String,
    val age: Int
) {
    @JsonIgnore
    fun isAdult(): Boolean {
        return age >= 20
    }
}
