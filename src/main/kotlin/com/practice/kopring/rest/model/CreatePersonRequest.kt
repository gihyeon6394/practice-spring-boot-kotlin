package com.practice.kopring.rest.model;

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls

/**
 * @author gihyeon-kim
 */

// curl
// curl --request POST \
//   --url http://localhost:8080/createPerson \
//   --header 'Content-Type: application/json' \
//   --data '{
// 	"name": "Kim",
// 	"age": 20,
// 	"carList": [ { "name": "BMW", "color": "blue" }, { "name": "Benz", "color": "black" }]
// }'
data class CreatePersonRequest(
    val name: String,
    val age: Int,
    // @JsonSetter(contentNulls = Nulls.SKIP) // null이면 무시
    @JsonSetter(contentNulls = Nulls.FAIL) // null이면 exception
    val carList: List<Car> = emptyList(),
)

data class Car(
    val name: String,
    val color: String,
)


