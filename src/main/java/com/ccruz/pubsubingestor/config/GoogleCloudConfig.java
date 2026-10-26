package com.ccruz.pubsubingestor.config;

import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleCloudConfig {

    @Bean
    BigQuery bigQuery(BigQueryProperties properties) {
        return BigQueryOptions.newBuilder()
                .setProjectId(properties.projectId())
                .build()
                .getService();
    }
}