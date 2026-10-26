package com.ccruz.pubsubingestor.service;

import com.ccruz.pubsubingestor.model.IncomingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PubSubEventDecoder {

    private final ObjectMapper objectMapper;
    private final Validator validator;

    public PubSubEventDecoder(
            ObjectMapper objectMapper,
            Validator validator
    ) {
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    public IncomingEvent decode(String encodedPayload) {
        try {
            String decodedPayload = new String(
                    Base64.getDecoder().decode(encodedPayload),
                    StandardCharsets.UTF_8
            );

            IncomingEvent event =
                    objectMapper.readValue(decodedPayload, IncomingEvent.class);

            validate(event);

            return event;
        } catch (IllegalArgumentException | JsonProcessingException exception) {
            throw new InvalidEventException(
                    "Pub/Sub message payload is not a valid event",
                    exception
            );
        }
    }

    private void validate(IncomingEvent event) {
        Set<ConstraintViolation<IncomingEvent>> violations =
                validator.validate(event);

        if (!violations.isEmpty()) {
            String description = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .sorted()
                    .collect(Collectors.joining(", "));

            throw new InvalidEventException(
                    "Event validation failed: " + description
            );
        }
    }
}