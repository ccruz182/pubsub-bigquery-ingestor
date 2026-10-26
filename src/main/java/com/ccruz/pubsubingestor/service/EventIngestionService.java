package com.ccruz.pubsubingestor.service;

import com.ccruz.pubsubingestor.config.BigQueryProperties;
import com.ccruz.pubsubingestor.model.IncomingEvent;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.InsertAllRequest;
import com.google.cloud.bigquery.InsertAllResponse;
import com.google.cloud.bigquery.TableId;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@Service
public class EventIngestionService {

    private final BigQuery bigQuery;
    private final TableId tableId;
    private final Clock clock;

    public EventIngestionService(
            BigQuery bigQuery,
            BigQueryProperties properties,
            Clock clock
    ) {
        this.bigQuery = bigQuery;
        this.tableId = TableId.of(
                properties.projectId(),
                properties.dataset(),
                properties.table()
        );
        this.clock = clock;
    }

    public void ingest(IncomingEvent event) {
        Map<String, Object> row = Map.of(
                "event_id", event.eventId(),
                "customer_id", event.customerId(),
                "event_type", event.eventType(),
                "occurred_at", event.occurredAt().toString(),
                "received_at", Instant.now(clock).toString()
        );

        InsertAllRequest request = InsertAllRequest.newBuilder(tableId)
                .addRow(event.eventId(), row)
                .build();

        InsertAllResponse response = bigQuery.insertAll(request);

        if (response.hasErrors()) {
            throw new BigQueryInsertException(
                    "BigQuery rejected one or more rows: "
                            + response.getInsertErrors()
            );
        }
    }

    public static class BigQueryInsertException extends RuntimeException {

        public BigQueryInsertException(String message) {
            super(message);
        }
    }
}