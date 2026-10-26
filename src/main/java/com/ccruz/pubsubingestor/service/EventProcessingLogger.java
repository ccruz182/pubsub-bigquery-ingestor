package com.ccruz.pubsubingestor.service;

import com.ccruz.pubsubingestor.model.IncomingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EventProcessingLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(EventProcessingLogger.class);

    public void processed(String messageId, IncomingEvent event) {
        LOGGER.atInfo()
                .addKeyValue("messageId", messageId)
                .addKeyValue("eventId", event.eventId())
                .addKeyValue("eventType", event.eventType())
                .log("Pub/Sub event processed and stored in BigQuery");
    }

    public void invalid(String messageId, Exception exception) {
        LOGGER.atWarn()
                .addKeyValue("messageId", messageId)
                .addKeyValue("errorType", exception.getClass().getSimpleName())
                .setCause(exception)
                .log("Invalid Pub/Sub event acknowledged without retry");
    }

    public void failed(String messageId, Exception exception) {
        LOGGER.atError()
                .addKeyValue("messageId", messageId)
                .addKeyValue("errorType", exception.getClass().getSimpleName())
                .setCause(exception)
                .log("Pub/Sub event processing failed; request will be retried");
    }
}