package de.mle.kotlin

import io.micrometer.observation.annotation.Observed
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Service(@Autowired private val subService: SubService) {
    private val log = KotlinLogging.logger {}

    @Observed(contextualName = "do-something")
    fun doSomething(id: String) {
        log.info { "Id to work with was: $id" }
        subService.doSomethingMore(id)
    }
}