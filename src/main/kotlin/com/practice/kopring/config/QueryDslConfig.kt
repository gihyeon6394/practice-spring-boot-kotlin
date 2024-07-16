package com.practice.kopring.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author gihyeon-kim
 */
@Configuration
class QueryDslConfig (
    val em: EntityManager
) {

    @Bean
    fun queryFactory(): JPAQueryFactory {
        return JPAQueryFactory(em)
    }
}
