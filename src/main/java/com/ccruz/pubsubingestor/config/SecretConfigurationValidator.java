package com.ccruz.pubsubingestor.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretConfigurationValidator {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(SecretConfigurationValidator.class);

    @Bean
    ApplicationRunner validateSecretConfiguration(SecretProperties properties) {
        return arguments -> LOGGER.info(
                "Secret Manager integration is configured; secret length={}",
                properties.integrationDemoSecret().length()
        );
    }
}