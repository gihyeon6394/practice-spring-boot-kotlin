package com.practice.kopring.config

import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

/**
 * @author gihyeon-kim
 */
@Configuration
class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("datasource.kopring")
    fun firstDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @Primary
    @ConfigurationProperties("datasource.kopring.configuration")
    fun kopringDataSource(firstDataSourceProperties: DataSourceProperties): HikariDataSource {
        return firstDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    @Bean
    @ConfigurationProperties("datasource.toybatch")
    fun secondDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean
    @ConfigurationProperties("datasource.toybatch.configuration")
    fun toyBatchDataSource(secondDataSourceProperties: DataSourceProperties): HikariDataSource {
        return secondDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource::class.java).build()
    }

    /**
     * @see : https://docs.spring.io/spring-boot/how-to/data-access.html#howto.data-access.use-multiple-entity-managers
     */
    @Bean
    @ConfigurationProperties("spring.jpa")
    fun jpaProperties(): JpaProperties {
        return JpaProperties().also {
            it.properties["hibernate.physical_naming_strategy"] = "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
            it.properties["hibernate.ddl-auto"] = "validate"
        }
    }
}
