package com.ngvgroup.bpm.core.logging.audit.config;

import com.ngvgroup.bpm.core.logging.activity.toggle.LoggingToggleProvider;
import com.ngvgroup.bpm.core.logging.kafka.service.LoggingKafkaProducerService;
import com.ngvgroup.bpm.core.logging.audit.listener.EntityChangeEventListener;
import com.ngvgroup.bpm.core.logging.audit.service.EntityChangeHandler;
import com.ngvgroup.bpm.core.logging.audit.service.EntityChangeService;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration(after = HibernateJpaAutoConfiguration.class)
@ConditionalOnClass({EntityManagerFactory.class, EventListenerRegistry.class})
@ConditionalOnBean({EntityManagerFactory.class})
public class HibernateAuditAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(HibernateAuditAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean({EntityChangeHandler.class})
    public EntityChangeHandler entityChangeHandler(LoggingToggleProvider toggleProvider, LoggingKafkaProducerService kafka) {
        log.info("[AUDIT] Creating default EntityChangeHandler (EntityChangeService)");
        return new EntityChangeService(toggleProvider, kafka);
    }

    @Bean
    public InitializingBean hibernateAuditInitializer(EntityManagerFactory emf, EntityChangeHandler handler) {
        return () -> {
            try {
                log.info("[AUDIT] hibernateAuditInitializer starting...");
                SessionFactoryImplementor sfi = emf.unwrap(SessionFactoryImplementor.class);
                EventListenerRegistry registry =
                        sfi.getServiceRegistry().getService(EventListenerRegistry.class);

                EntityChangeEventListener listener = new EntityChangeEventListener(handler);

                registry.getEventListenerGroup(EventType.PRE_INSERT).appendListener(listener);
                registry.getEventListenerGroup(EventType.PRE_UPDATE).appendListener(listener);
                registry.getEventListenerGroup(EventType.PRE_DELETE).appendListener(listener);

                log.info("[AUDIT] Hibernate audit listeners registered (PRE_INSERT / PRE_UPDATE / PRE_DELETE)");
            } catch (Exception e) {
                log.error("[AUDIT] Failed to register Hibernate listeners", e);
            }
        };
    }
}
