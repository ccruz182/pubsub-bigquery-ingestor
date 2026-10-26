package com.ccruz.pubsubingestor.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record IncomingEvent(

        @NotBlank
        String eventId,

        @NotBlank
        String customerId,

        @NotBlank
        String eventType,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        Instant occurredAt
) {
}