package com.practice.kopring.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

/**
 * @author gihyeon-kim
 */
@Configuration(proxyBeanMethods = false)
@EnableJpaRepositories(
    basePackages = ["com.practice.kopring.batch"],
    entityManagerFactoryRef = "toyBatchEntityManagerFactory"
)
class ToyBatchEntityMangerConfig {

    @Bean
    fun toyBatchEntityManagerFactory(
        @Qualifier("toyBatchDataSource") toyBatchDataSource: DataSource,
        jpaProperties: JpaProperties,
    ): LocalContainerEntityManagerFactoryBean {
        return createEntityManagerFactoryBuilder(jpaProperties).dataSource(toyBatchDataSource)
            .packages("com.practice.kopring.batch")
            .persistenceUnit("toyBatchEntityManager").build()
    }

    private fun createEntityManagerFactoryBuilder(jpaProperties: JpaProperties): EntityManagerFactoryBuilder {
        val jpaVendorAdapter = createJpaVendorAdapter(jpaProperties)
        jpaProperties.properties["hibernate.physical_naming_strategy"] =
            "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        return EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.properties, null)
    }

    private fun createJpaVendorAdapter(jpaProperties: JpaProperties): JpaVendorAdapter {
        // ... map JPA properties as needed
        return HibernateJpaVendorAdapter()
    }

}
