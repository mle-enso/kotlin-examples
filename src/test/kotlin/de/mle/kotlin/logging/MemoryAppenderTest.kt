package de.idealo.orca.cataloging.logging

import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.spi.LoggingEvent
import ch.qos.logback.classic.util.LogbackMDCAdapter
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.Date

internal class MemoryAppenderTest {
    @Test
    internal fun append() {
        // given
        val appender = MemoryAppender()
        appender.start()
        val event = LoggingEvent()
        val message = javaClass.name + " test " + Date()
        event.message = message
        val loggerContext = LoggerContext()
        loggerContext.mdcAdapter = LogbackMDCAdapter()
        event.setLoggerContext(loggerContext)

        // when
        appender.append(event)

        // then
        assertThat(MemoryAppender.LOG_MESSAGES).anyMatch { e -> e.message.contains(message) }
    }
}
