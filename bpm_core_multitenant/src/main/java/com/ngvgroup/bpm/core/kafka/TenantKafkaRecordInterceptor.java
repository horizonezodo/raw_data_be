package com.ngvgroup.bpm.core.kafka;

import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import lombok.NonNull;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.kafka.listener.RecordInterceptor;

import java.nio.charset.StandardCharsets;


public record TenantKafkaRecordInterceptor<K, V>(MultitenancyProperties mtProps) implements RecordInterceptor<K, V> {

    @Override
    public ConsumerRecord<K, V> intercept(@NonNull ConsumerRecord<K, V> record, @NonNull Consumer<K, V> consumer) {
        if (!mtProps.isEnabled()) return record;

        String headerKey = mtProps.getTenantHeader();
        Header h = record.headers().lastHeader(headerKey);

        String tenantId = null;
        if (h != null && h.value() != null) {
            tenantId = new String(h.value(), StandardCharsets.UTF_8).trim();
        }

        // ✅ multi=true mà thiếu header -> throw 
        if (tenantId == null || tenantId.isBlank()) {
            throw new IllegalArgumentException(
                    "Missing tenant header '" + headerKey + "' for Kafka record. " +
                            "topic=" + record.topic() + ", partition=" + record.partition() + ", offset=" + record.offset()
            );
        }
        TenantContext.setTenantId(tenantId);
        return record;
    }

    @Override
    public void afterRecord(@NonNull ConsumerRecord record, @NonNull Consumer consumer) {
        TenantContext.clear();
    }
}
