package com.ccruz.pubsubingestor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record PubSubPushRequest(

        @NotNull
        @Valid
        PubSubMessage message,

        String subscription
) {

    public record PubSubMessage(

            @NotBlank
            String data,

            @JsonProperty("messageId")
            @NotBlank
            String messageId,

            @JsonProperty("publishTime")
            String publishTime,

            Map<String, String> attributes
    ) {
    }
}