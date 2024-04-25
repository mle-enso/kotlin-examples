package de.mle.kotlin

import io.micrometer.core.aop.TimedAspect
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.observation.ObservationRegistry
import io.micrometer.observation.aop.ObservedAspect
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy


@Configuration
@EnableAspectJAutoProxy  // especially to enable @Timed-measuring on classes not covered by org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter
class AspectJConfig {

    /**
     * Especially to enable `@Timed`-measuring on classes not covered by org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.
     * WebMvcMetricsFilter only picks `@Timed` annotations on controller handler methods.
     * This aspect serves as enabler of `@Timed`-measuring also on service classes.
     * See also https://github.com/micrometer-metrics/micrometer/pull/395#issuecomment-366828578
     */
    @Bean
    fun timedAspect(registry: MeterRegistry): TimedAspect {
        return TimedAspect(registry)
    }

    @Bean
    fun observedAspect(observationRegistry: ObservationRegistry): ObservedAspect {
        return ObservedAspect(observationRegistry)
    }
}
