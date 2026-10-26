package com.ccruz.pubsubingestor.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record PubSubPushRequest(

        @NotNull
        @Valid
        PubSubMessage message,

        String subscription
) {

    public record PubSubMessage(

            @NotNull
            String data,

            @JsonProperty("messageId")
            String messageId,

            @JsonProperty("publishTime")
            String publishTime
    ) {
    }
}