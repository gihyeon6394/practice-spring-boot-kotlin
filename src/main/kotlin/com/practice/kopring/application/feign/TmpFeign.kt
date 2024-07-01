package com.practice.kopring.application.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

/**
 * @author gihyeon-kim
 */
@FeignClient(name = "tmp", url = "http://localhost:8080")
interface TmpFeign {

    @GetMapping("/testReadTimeout")
    fun testReadTimeout(): String
}
