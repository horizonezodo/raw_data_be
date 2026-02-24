package com.ngvgroup.bpm.core.autoconfigure;

import com.ngvgroup.bpm.core.kafka.TenantKafkaProducerInterceptor;
import com.ngvgroup.bpm.core.kafka.TenantKafkaRecordInterceptor;
import com.ngvgroup.bpm.core.persistence.config.MultitenancyProperties;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;

import java.util.*;

@AutoConfiguration
@ConditionalOnClass(ConcurrentKafkaListenerContainerFactory.class)
@EnableConfigurationProperties(MultitenancyProperties.class)
@RequiredArgsConstructor
public class BpmCoreKafkaMultitenantAutoConfiguration {

    private final MultitenancyProperties mtProps;

    @Bean
    public TenantKafkaRecordInterceptor tenantKafkaRecordInterceptor() {
        return new TenantKafkaRecordInterceptor(mtProps);
    }

    /** ✅ Consumer: patch mọi listener factory để auto set/clear TenantContext */
    @Bean
    public BeanPostProcessor tenantKafkaListenerFactoryBpp(TenantKafkaRecordInterceptor interceptor) {
        return new BeanPostProcessor() {
            @Override
            @SuppressWarnings({"unchecked"})
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {
                if (bean instanceof ConcurrentKafkaListenerContainerFactory<?, ?> factory) {
                    @SuppressWarnings({"rawtypes"})
                    ConcurrentKafkaListenerContainerFactory raw = factory;
                    raw.setRecordInterceptor(interceptor);
                }
                return bean;
            }
        };
    }

    /** ✅ Producer: patch DefaultKafkaProducerFactory để auto add ProducerInterceptor */
    @Bean
    public BeanPostProcessor tenantKafkaProducerFactoryBpp() {
        // set props cho interceptor (dùng TenantContext + tenantHeader)
        TenantKafkaProducerInterceptor.setProps(mtProps);

        return new BeanPostProcessor() {
            @Override
            public Object postProcessAfterInitialization(@NonNull Object bean, @NonNull String beanName) throws BeansException {

                if (bean instanceof DefaultKafkaProducerFactory<?, ?> dpf) {
                    Map<String, Object> cfg = new HashMap<>(dpf.getConfigurationProperties());

                    Object existing = cfg.get(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG);
                    List<String> list = normalize(existing);

                    String clazz = TenantKafkaProducerInterceptor.class.getName();
                    if (!list.contains(clazz)) list.add(clazz);

                    cfg.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, list);
                    dpf.updateConfigs(cfg);
                }

                return bean;
            }

            private List<String> normalize(Object existing) {
                List<String> list = new ArrayList<>();
                if (existing == null) return list;

                if (existing instanceof String s) {
                    for (String x : s.split(",")) if (!x.isBlank()) list.add(x.trim());
                } else if (existing instanceof List<?> l) {
                    for (Object o : l) if (o != null && !o.toString().isBlank()) list.add(o.toString().trim());
                } else {
                    list.add(existing.toString().trim());
                }
                return list;
            }
        };
    }
}
