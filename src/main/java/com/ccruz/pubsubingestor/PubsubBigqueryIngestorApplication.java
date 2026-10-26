package com.ccruz.pubsubingestor;

import com.ccruz.pubsubingestor.config.BigQueryProperties;
import com.ccruz.pubsubingestor.config.SecretProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        BigQueryProperties.class,
        SecretProperties.class
})public class PubsubBigqueryIngestorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubsubBigqueryIngestorApplication.class, args);
    }
}