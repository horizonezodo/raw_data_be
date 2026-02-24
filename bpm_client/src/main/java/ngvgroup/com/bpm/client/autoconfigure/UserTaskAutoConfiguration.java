package ngvgroup.com.bpm.client.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.annotation.UserTaskSubscription;
import ngvgroup.com.bpm.client.template.AbstractUserTask;
import ngvgroup.com.bpm.client.template.UserTaskRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Map;

/**
 * Auto-configuration for User Task handlers.
 * <p>
 * Automatically discovers all beans annotated with {@link UserTaskSubscription}
 * and registers them with {@link UserTaskRegistry} when the application starts.
 */
@Slf4j
@Configuration
public class UserTaskAutoConfiguration {

    private final UserTaskRegistry userTaskRegistry;
    private final ApplicationContext applicationContext;

    @Autowired
    public UserTaskAutoConfiguration(UserTaskRegistry userTaskRegistry,
            ApplicationContext applicationContext) {
        this.userTaskRegistry = userTaskRegistry;
        this.applicationContext = applicationContext;
    }

    /**
     * Discovers and registers all beans annotated with @UserTaskSubscription when
     * the application is ready.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void registerHandlers() {
        Map<String, Object> annotatedBeans = applicationContext.getBeansWithAnnotation(UserTaskSubscription.class);

        if (annotatedBeans.isEmpty()) {
            log.info("No @UserTaskSubscription beans found to register");
            return;
        }

        log.info("Discovering {} @UserTaskSubscription bean(s)...", annotatedBeans.size());

        for (Map.Entry<String, Object> entry : annotatedBeans.entrySet()) {
            String beanName = entry.getKey();
            Object bean = entry.getValue();

            if (!(bean instanceof AbstractUserTask<?>)) {
                log.warn(
                        "Bean '{}' is annotated with @UserTaskSubscription but does not extend AbstractUserTask. Skipping.",
                        beanName);
                continue;
            }

            UserTaskSubscription annotation = AnnotationUtils.findAnnotation(bean.getClass(),
                    UserTaskSubscription.class);

            if (annotation == null) {
                log.warn("Cannot find @UserTaskSubscription annotation on bean '{}'. Skipping.", beanName);
                continue;
            }

            try {
                String taskDefKey = annotation.value();

                @SuppressWarnings("unchecked")
                AbstractUserTask<?> handler = (AbstractUserTask<?>) bean;

                userTaskRegistry.register(taskDefKey, handler);

                log.info("Registered UserTask handler: taskDefinitionKey='{}', class={}",
                        taskDefKey, bean.getClass().getSimpleName());

            } catch (Exception e) {
                log.error("Failed to register handler bean '{}': {}", beanName, e.getMessage(), e);
            }
        }

        log.info("UserTaskHandler registration complete. Total registered: {}",
                userTaskRegistry.getHandlerCount());
    }
}
