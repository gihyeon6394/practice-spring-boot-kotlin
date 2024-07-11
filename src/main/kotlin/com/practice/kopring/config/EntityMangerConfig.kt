package com.practice.kopring.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import javax.sql.DataSource


/**
 * @author gihyeon-kim
 */
@Configuration(proxyBeanMethods = false)
class EntityMangerConfig {

    // 각 datasource가 공통으로 쓸 jpa properties를 설정
    @Bean
    @ConfigurationProperties("spring.jpa")
    fun jpaProperties(): JpaProperties {
        return JpaProperties()
    }

    @Bean(name = ["kopringEntityManagerFactory"])
    @Primary
    fun kopringEntityManagerFactory(
        @Qualifier("kopringDataSource") dataSource: DataSource,
        entityManagerFactoryBuilder: EntityManagerFactoryBuilder,
        jpaProperties: JpaProperties
    ): LocalContainerEntityManagerFactoryBean {
        return entityManagerFactoryBuilder
            .dataSource(dataSource)
            .properties(jpaProperties.properties)
            .packages("com.practice.kopring.application")
            .persistenceUnit("kopringEntityManager")
            .build()
    }

    @Bean(name = ["toyBatchEntityManagerFactory"])
    fun toyBatchEntityManagerFactory(
        @Qualifier("toyBatchDataSource") dataSource: DataSource,
        entityManagerFactoryBuilder: EntityManagerFactoryBuilder,
        jpaProperties: JpaProperties
    ): LocalContainerEntityManagerFactoryBean {
        return entityManagerFactoryBuilder
            .dataSource(dataSource)
            .properties(jpaProperties.properties)
            .packages("com.practice.kopring.batch")
            .persistenceUnit("toyBatchEntityManager")
            .build()
    }

}
