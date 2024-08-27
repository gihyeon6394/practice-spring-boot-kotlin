package com.practice.kopring.config

import io.micrometer.core.instrument.Gauge
import io.micrometer.core.instrument.MeterRegistry
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * @author gihyeon-kim
 */
@Configuration(proxyBeanMethods = false)
class ThreadMeterRegistryConfiguration {

    @Bean
    fun threadPoolA() = ThreadPoolTaskExecutor().apply {
        corePoolSize = 5
        maxPoolSize = 5
        setThreadNamePrefix("threadPoolA-")
        setWaitForTasksToCompleteOnShutdown(true)
        setAwaitTerminationSeconds(60)
        initialize()
    }

    @Bean
    fun threadPoolB() = ThreadPoolTaskExecutor().apply {
        corePoolSize = 10
        maxPoolSize = 10
        setThreadNamePrefix("threadPoolB-")
        setWaitForTasksToCompleteOnShutdown(true)
        setAwaitTerminationSeconds(60)
        initialize()
    }

    @Bean
    fun metricsThreadPool(
        @Qualifier("threadPoolA") threadPoolA: ThreadPoolTaskExecutor,
        @Qualifier("threadPoolB") threadPoolB: ThreadPoolTaskExecutor,
    ): MeterRegistryCustomizer<MeterRegistry> {
        return MeterRegistryCustomizer { registry: MeterRegistry? ->
            listOf(threadPoolA, threadPoolB).forEach { threadPool ->
                Gauge.builder(
                    "custom.executor.${threadPool.threadNamePrefix}.max.threads", threadPool
                ) { obj: ThreadPoolTaskExecutor ->
                    obj.maxPoolSize.toDouble()
                }.tags("name", "custom.executor.${threadPool.threadNamePrefix}.max.threads").register(registry!!)
                Gauge.builder(
                    "custom.executor.${threadPool.threadNamePrefix}.active.threads", threadPool
                ) { obj: ThreadPoolTaskExecutor ->
                    obj.activeCount.toDouble()
                }.tags("name", "customTaskExecutor").register(registry!!)
                Gauge.builder(
                    "custom.executor.${threadPool.threadNamePrefix}.pool.size", threadPool
                ) { obj: ThreadPoolTaskExecutor ->
                    obj.poolSize.toDouble()
                }.tags("name", "custom.executor.${threadPool.threadNamePrefix}.pool.size").register(registry)
                Gauge.builder(
                    "custom.executor.${threadPool.threadNamePrefix}.queue.size", threadPool
                ) { obj: ThreadPoolTaskExecutor ->
                    obj.threadPoolExecutor.queue.size.toDouble()
                }.tags("name", "custom.executor.${threadPool.threadNamePrefix}.queue.size").register(registry)
                Gauge.builder(
                    "custom.executor.${threadPool.threadNamePrefix}.queue.remaining.capacity", threadPool
                ) { obj: ThreadPoolTaskExecutor ->
                    obj.threadPoolExecutor.queue.remainingCapacity().toDouble()
                }.tags("name", "custom.executor.${threadPool.threadNamePrefix}.queue.remaining.capacity")
                    .register(registry)
            }
        }
    }
}
