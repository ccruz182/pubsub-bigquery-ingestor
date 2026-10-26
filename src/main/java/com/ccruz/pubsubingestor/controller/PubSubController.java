package com.ccruz.pubsubingestor.controller;

import com.ccruz.pubsubingestor.model.IncomingEvent;
import com.ccruz.pubsubingestor.model.PubSubPushRequest;
import com.ccruz.pubsubingestor.service.EventIngestionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
public class PubSubController {

    private final ObjectMapper objectMapper;
    private final EventIngestionService eventIngestionService;

    public PubSubController(
            ObjectMapper objectMapper,
            EventIngestionService eventIngestionService
    ) {
        this.objectMapper = objectMapper;
        this.eventIngestionService = eventIngestionService;
    }

    @PostMapping("/pubsub/events")
    public ResponseEntity<Void> receive(@Valid @RequestBody PubSubPushRequest request)
            throws JsonProcessingException {

        String decodedPayload = new String(
                Base64.getDecoder().decode(request.message().data()),
                StandardCharsets.UTF_8
        );

        IncomingEvent event = objectMapper.readValue(decodedPayload, IncomingEvent.class);

        eventIngestionService.ingest(event);

        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            JsonProcessingException.class
    })
    public ResponseEntity<Void> handleInvalidMessage(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}