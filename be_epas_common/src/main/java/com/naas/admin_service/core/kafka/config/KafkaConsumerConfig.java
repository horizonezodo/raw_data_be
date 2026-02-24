package com.naas.admin_service.core.kafka.config;

import com.naas.admin_service.features.section.kafka.UserSessionEvent;
import com.ngvgroup.bpm.core.kafka.TenantKafkaRecordInterceptor;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${logging.kafka.consumer-group:activity-log-consumer}")
    private String consumerGroup;

    @Bean
    public ConsumerFactory<String, String> activityLogConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> activityLogKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(activityLogConsumerFactory());
        return factory;
    }

    // ====== FACTORY CHO JOB (mới thêm) ======

    @Bean
    public ConsumerFactory<String, String> jobConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // ⚠️ KHÔNG set GROUP_ID_CONFIG ở đây
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> jobKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(jobConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, UserSessionEvent> userSessionEventProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, UserSessionEvent> userSessionEventKafkaTemplate() {
        return new KafkaTemplate<>(userSessionEventProducerFactory());
    }

    // Consumer cho UserSessionEvent
    @Bean
    public ConsumerFactory<String, UserSessionEvent> userSessionEventConsumerFactory() {
        JsonDeserializer<UserSessionEvent> deserializer = new JsonDeserializer<>(UserSessionEvent.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    /**
     * ContainerFactory riêng cho WS topic.
     * - Nếu bạn muốn strict tenant header ở topic WS -> setRecordInterceptor(strict) ở đây.
     * - Và vì lib-core BPP đã "không override nếu đã set", nên interceptor này sẽ được giữ nguyên.
     */
    @Bean(name = "userSessionEventListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, UserSessionEvent> userSessionEventListenerContainerFactory(
            ConsumerFactory<String, UserSessionEvent> userSessionEventConsumerFactory,
            MultitenancyProperties mtProps
    ) {
        ConcurrentKafkaListenerContainerFactory<String, UserSessionEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(userSessionEventConsumerFactory);

        // ✅ Strict: multi=true mà thiếu tenant header -> throw ngay để không CRUD nhầm DB
        factory.setRecordInterceptor(new TenantKafkaRecordInterceptor<>(mtProps));

        return factory;
    }
}