package de.idealo.orca.cataloging.logging

import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.UnsynchronizedAppenderBase
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class MemoryAppender : UnsynchronizedAppenderBase<ILoggingEvent>() {

    companion object {
        val LOG_MESSAGES: Queue<ILoggingEvent> = ConcurrentLinkedQueue()
    }

    public override fun append(event: ILoggingEvent) {
        if (!isStarted) {
            return
        }

        event.prepareForDeferredProcessing()
        LOG_MESSAGES.add(event)
    }
}
