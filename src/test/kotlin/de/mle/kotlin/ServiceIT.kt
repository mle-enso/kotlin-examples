package de.mle.kotlin

import de.idealo.orca.cataloging.logging.MemoryAppender
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureObservability
class ServiceIT(
    @Autowired private val service: Service
) {
    @Test
    fun refresh() {
        // when
        service.doSomething("123abc")

        // then
        Awaitility.await().untilAsserted {
            val filteredLogs = MemoryAppender.LOG_MESSAGES.filter {
                it.loggerName == Service::class.java.name || it.loggerName == SubService::class.java.name
            }
            assertThat(filteredLogs[0].formattedMessage).isEqualTo("Id to work with was: 123abc")
            assertThat(filteredLogs[1].formattedMessage).isEqualTo("Id to work more with was: 123abc")

            val firstTraceId = filteredLogs[0].mdcPropertyMap["traceId"]
            val firstSpanId = filteredLogs[0].mdcPropertyMap["spanId"]
            val secondTraceId = filteredLogs[1].mdcPropertyMap["traceId"]
            val secondSpanId = filteredLogs[1].mdcPropertyMap["spanId"]

            assertThat(firstTraceId).isEqualTo(secondTraceId)
            assertThat(firstSpanId).isNotEqualTo(secondSpanId)
        }
    }
}