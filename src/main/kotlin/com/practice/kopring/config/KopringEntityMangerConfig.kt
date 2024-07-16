package com.practice.kopring.config

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

/**
 * @author gihyeon-kim
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(basePackages = ["com.practice.kopring.application"], entityManagerFactoryRef = "kopringEntityManagerFactory")
class KopringEntityMangerConfig {

    @Bean
    @Primary
    fun kopringEntityManagerFactory(
        kopringDataSource: DataSource?,
        jpaProperties: JpaProperties
    ): LocalContainerEntityManagerFactoryBean {
        val builder = createEntityManagerFactoryBuilder(jpaProperties)
        return builder.dataSource(kopringDataSource).packages("com.practice.kopring.application").persistenceUnit("kopringDs").build()
    }

    private fun createEntityManagerFactoryBuilder(jpaProperties: JpaProperties): EntityManagerFactoryBuilder {
        val jpaVendorAdapter = createJpaVendorAdapter(jpaProperties)
        return EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.properties, null)
    }

    private fun createJpaVendorAdapter(jpaProperties: JpaProperties): JpaVendorAdapter {
        return HibernateJpaVendorAdapter()
    }

}
