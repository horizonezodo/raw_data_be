package com.ngvgroup.bpm.core.kafka;

import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import com.ngvgroup.bpm.core.persistence.config.TenantContext;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class TenantKafkaProducerInterceptor implements ProducerInterceptor<Object, Object> {

    private static volatile MultitenancyProperties mtProps;

    public static void setProps(MultitenancyProperties p) {
        mtProps = p;
    }

    @Override
    public ProducerRecord<Object, Object> onSend(ProducerRecord<Object, Object> record) {
        MultitenancyProperties p = mtProps;
        if (p == null || !p.isEnabled()) return record;

        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || tenantId.isBlank()) return record;

        record.headers().add(new RecordHeader(
                p.getTenantHeader(),
                tenantId.trim().getBytes(StandardCharsets.UTF_8)
        ));
        return record;
    }

    @Override public void onAcknowledgement(RecordMetadata metadata, Exception exception) {}
    @Override public void close() {}
    @Override public void configure(Map<String, ?> configs) {}
}
