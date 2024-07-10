package com.practice.kopring.config

import org.springframework.beans.factory.annotation.Qualifier
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

    @Bean(name = ["kopringEntityManagerFactory"])
    @Primary
    fun kopringEntityManagerFactory(
        @Qualifier("kopringDataSource") dataSource: DataSource,
        entityManagerFactoryBuilder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean {
        return entityManagerFactoryBuilder
            .dataSource(dataSource)
            .packages("com.practice.kopring.application")
            .persistenceUnit("kopringEntityManager")
            .build()
    }

    @Bean(name = ["toyBatchEntityManagerFactory"])
    fun toyBatchEntityManagerFactory(
        @Qualifier("toyBatchDataSource") dataSource: DataSource,
        entityManagerFactoryBuilder: EntityManagerFactoryBuilder,
    ): LocalContainerEntityManagerFactoryBean {
        return entityManagerFactoryBuilder
            .dataSource(dataSource)
            .packages("com.practice.kopring.batch")
            .persistenceUnit("toyBatchEntityManager")
            .build()
    }

}
