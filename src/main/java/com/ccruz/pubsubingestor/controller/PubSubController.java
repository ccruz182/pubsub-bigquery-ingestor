package com.ccruz.pubsubingestor.controller;

import com.ccruz.pubsubingestor.model.IncomingEvent;
import com.ccruz.pubsubingestor.model.PubSubPushRequest;
import com.ccruz.pubsubingestor.service.EventIngestionService;
import com.ccruz.pubsubingestor.service.EventProcessingLogger;
import com.ccruz.pubsubingestor.service.InvalidEventException;
import com.ccruz.pubsubingestor.service.PubSubEventDecoder;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PubSubController {

    private final PubSubEventDecoder eventDecoder;
    private final EventIngestionService eventIngestionService;
    private final EventProcessingLogger eventProcessingLogger;

    public PubSubController(
            PubSubEventDecoder eventDecoder,
            EventIngestionService eventIngestionService,
            EventProcessingLogger eventProcessingLogger
    ) {
        this.eventDecoder = eventDecoder;
        this.eventIngestionService = eventIngestionService;
        this.eventProcessingLogger = eventProcessingLogger;
    }

    @PostMapping("/pubsub/events")
    public ResponseEntity<Void> receive(@Valid @RequestBody PubSubPushRequest request) {
        String messageId = request.message().messageId();

        try {
            IncomingEvent event = eventDecoder.decode(request.message().data());

            eventIngestionService.ingest(event);

            eventProcessingLogger.processed(messageId, event);

            return ResponseEntity.noContent().build();
        } catch (InvalidEventException exception) {
            eventProcessingLogger.invalid(messageId, exception);

            return ResponseEntity.internalServerError().build();
        } catch (RuntimeException exception) {
            eventProcessingLogger.failed(messageId, exception);

            return ResponseEntity.internalServerError().build();
        }
    }
}