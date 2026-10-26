package com.ccruz.pubsubingestor.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class GoogleCloudConfig {

    @Bean
    BigQuery bigQuery(BigQueryProperties properties) {
        return BigQueryOptions.newBuilder()
                .setProjectId(properties.projectId())
                .build()
                .getService();
    }

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}