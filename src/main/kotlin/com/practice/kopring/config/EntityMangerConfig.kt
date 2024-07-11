package com.practice.kopring.config

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


/**
 * @author gihyeon-kim
 */
@Configuration(proxyBeanMethods = false)
class EntityMangerConfig {

    @Bean
    @ConfigurationProperties("spring.jpa")
    fun jpaProperties(): JpaProperties {
        return JpaProperties()
    }
}
