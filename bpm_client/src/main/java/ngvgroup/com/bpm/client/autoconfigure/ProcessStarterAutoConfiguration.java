package ngvgroup.com.bpm.client.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription;
import ngvgroup.com.bpm.client.template.AbstractProcessStarter;
import ngvgroup.com.bpm.client.template.ProcessStarterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

/**
 * Auto-configuration for Process Starter handlers.
 * <p>
 * Automatically discovers all beans annotated with
 * {@link ProcessStarterSubscription}
 * and registers them with {@link ProcessStarterRegistry} when the application
 * starts.
 */
@Slf4j
@Configuration
public class ProcessStarterAutoConfiguration {

    private final ProcessStarterRegistry processStarterRegistry;
    private final ApplicationContext applicationContext;

    @Autowired
    public ProcessStarterAutoConfiguration(ProcessStarterRegistry processStarterRegistry,
            ApplicationContext applicationContext) {
        this.processStarterRegistry = processStarterRegistry;
        this.applicationContext = applicationContext;
    }

    /**
     * Discovers and registers all beans annotated with @ProcessStarterSubscription
     * when the application is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void registerHandlers() {
        Map<String, Object> annotatedBeans = applicationContext
                .getBeansWithAnnotation(ProcessStarterSubscription.class);

        if (annotatedBeans.isEmpty()) {
            log.info("No @ProcessStarterSubscription beans found to register");
            return;
        }

        log.info("Discovering {} @ProcessStarterSubscription bean(s)...", annotatedBeans.size());

        for (Map.Entry<String, Object> entry : annotatedBeans.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();

            if (!(bean instanceof AbstractProcessStarter<?>)) {
                log.warn(
                        "Bean '{}' is annotated with @ProcessStarterSubscription but does not extend AbstractProcessStarter. Skipping.",
                        beanName);
                continue;
            }

            ProcessStarterSubscription annotation = AnnotationUtils.findAnnotation(bean.getClass(),
                    ProcessStarterSubscription.class);

            if (annotation == null) {
                log.warn("Cannot find @ProcessStarterSubscription annotation on bean '{}'. Skipping.", beanName);
                continue;
            }

            try {
                String processDefKey = annotation.value();

                @SuppressWarnings("unchecked")
                AbstractProcessStarter<?> handler = (AbstractProcessStarter<?>) bean;

                processStarterRegistry.register(processDefKey, handler);

                log.info("Registered ProcessStarter handler: processDefinitionKey='{}', class={}",
                        processDefKey, bean.getClass().getSimpleName());

            } catch (Exception e) {
                log.error("Failed to register handler bean '{}': {}", beanName, e.getMessage(), e);
            }
        }

        log.info("ProcessStarter registration complete. Total registered: {}",
                processStarterRegistry.getHandlerCount());
    }
}
