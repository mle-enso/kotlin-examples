package de.mle.kotlin

import io.micrometer.observation.annotation.Observed
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class SubService {
    private val log = KotlinLogging.logger {}

    @Observed(contextualName = "do-something-more")
    fun doSomethingMore(id: String) {
        log.info { "Id to work more with was: $id" }
    }
}