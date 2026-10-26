package com.ccruz.pubsubingestor.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.secret")
public record SecretProperties(

        @NotBlank
        String integrationDemoSecret
) {
}