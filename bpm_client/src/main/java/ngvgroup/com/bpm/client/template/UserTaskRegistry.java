package ngvgroup.com.bpm.client.template;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing User Task handlers.
 * <p>
 * Handlers annotated with
 * {@link ngvgroup.com.bpm.client.annotation.UserTaskSubscription}
 * are automatically discovered and registered via
 * {@link ngvgroup.com.bpm.client.autoconfigure.UserTaskAutoConfiguration}.
 * <p>
 * Thread-safe implementation using ConcurrentHashMap.
 *
 * @see ngvgroup.com.bpm.client.annotation.UserTaskSubscription
 * @see ngvgroup.com.bpm.client.autoconfigure.UserTaskAutoConfiguration
 */
@Slf4j
@Component
public class UserTaskRegistry {

    /**
     * Map of taskDefinitionKey -> AbstractUserTask handler
     */
    private final Map<String, AbstractUserTask<?>> handlers = new ConcurrentHashMap<>();

    /**
     * Registers a handler for a specific task definition key.
     * Called by UserTaskAutoConfiguration after reading @UserTaskSubscription
     * annotation.
     *
     * @param taskDefinitionKey the BPMN task definition key (unique across all
     *                          processes)
     * @param handler           the handler instance
     * @throws IllegalArgumentException if taskDefinitionKey is null/empty or
     *                                  handler is null
     */
    public void register(String taskDefinitionKey, AbstractUserTask<?> handler) {
        if (taskDefinitionKey == null || taskDefinitionKey.isBlank()) {
            throw new IllegalArgumentException("taskDefinitionKey cannot be null or empty");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        AbstractUserTask<?> existing = handlers.put(taskDefinitionKey, handler);
        if (existing != null) {
            log.warn("Replaced existing handler for taskDefinitionKey '{}': {} -> {}",
                    taskDefinitionKey, existing.getClass().getSimpleName(), handler.getClass().getSimpleName());
        } else {
            log.debug("Registered handler for taskDefinitionKey '{}': {}",
                    taskDefinitionKey, handler.getClass().getSimpleName());
        }
    }

    /**
     * Unregisters a handler by its task definition key.
     *
     * @param taskDefinitionKey the task definition key
     * @return the removed handler, or null if not found
     */
    public AbstractUserTask<?> unregister(String taskDefinitionKey) {
        AbstractUserTask<?> removed = handlers.remove(taskDefinitionKey);
        if (removed != null) {
            log.info("Unregistered handler for taskDefinitionKey '{}'", taskDefinitionKey);
        }
        return removed;
    }

    /**
     * Gets a handler by its task definition key.
     *
     * @param taskDefinitionKey the task definition key
     * @return Optional containing the handler if found
     */
    public Optional<AbstractUserTask<?>> getHandler(String taskDefinitionKey) {
        return Optional.ofNullable(handlers.get(taskDefinitionKey));
    }

    /**
     * Gets a handler by its task definition key, throwing an exception if not
     * found.
     *
     * @param taskDefinitionKey the task definition key
     * @return the handler
     * @throws BusinessException if no handler is found
     */
    @SuppressWarnings("unchecked")
    public <T> AbstractUserTask<T> getHandlerOrThrow(String taskDefinitionKey) {
        return (AbstractUserTask<T>) getHandler(taskDefinitionKey)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BAD_REQUEST,
                        "Không tìm thấy handler cho taskDefinitionKey: " + taskDefinitionKey));
    }

    /**
     * Checks if a handler exists for the given task definition key.
     *
     * @param taskDefinitionKey the task definition key
     * @return true if a handler exists
     */
    public boolean hasHandler(String taskDefinitionKey) {
        return handlers.containsKey(taskDefinitionKey);
    }

    /**
     * Gets all registered handlers.
     *
     * @return unmodifiable collection of all handlers
     */
    public Collection<AbstractUserTask<?>> getAllHandlers() {
        return Collections.unmodifiableCollection(handlers.values());
    }

    /**
     * Gets all registered task definition keys.
     *
     * @return unmodifiable collection of all keys
     */
    public Collection<String> getAllTaskDefinitionKeys() {
        return Collections.unmodifiableCollection(handlers.keySet());
    }

    /**
     * Gets the count of registered handlers.
     *
     * @return the number of handlers
     */
    public int getHandlerCount() {
        return handlers.size();
    }
}
