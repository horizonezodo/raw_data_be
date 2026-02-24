package ngvgroup.com.bpm.client.template;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for managing Process Starter handlers.
 * <p>
 * Handlers annotated with
 * {@link ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription}
 * are automatically discovered and registered via
 * {@link ngvgroup.com.bpm.client.autoconfigure.ProcessStarterAutoConfiguration}.
 * <p>
 * Thread-safe implementation using ConcurrentHashMap.
 *
 * @see ngvgroup.com.bpm.client.annotation.ProcessStarterSubscription
 * @see ngvgroup.com.bpm.client.autoconfigure.ProcessStarterAutoConfiguration
 */
@Slf4j
@Component
public class ProcessStarterRegistry {

    /**
     * Map of processDefinitionKey -> AbstractProcessStarter handler
     */
    private final Map<String, AbstractProcessStarter<?>> handlers = new ConcurrentHashMap<>();

    /**
     * Registers a handler for a specific process definition key.
     * Called by ProcessStarterAutoConfiguration after
     * reading @ProcessStarterSubscription annotation.
     *
     * @param processDefinitionKey the BPMN process definition key
     * @param handler              the handler instance
     * @throws IllegalArgumentException if processDefinitionKey is null/empty or
     *                                  handler is null
     */
    public void register(String processDefinitionKey, AbstractProcessStarter<?> handler) {
        if (processDefinitionKey == null || processDefinitionKey.isBlank()) {
            throw new IllegalArgumentException("processDefinitionKey cannot be null or empty");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Handler cannot be null");
        }

        AbstractProcessStarter<?> existing = handlers.put(processDefinitionKey, handler);
        if (existing != null) {
            log.warn("Replaced existing handler for processDefinitionKey '{}': {} -> {}",
                    processDefinitionKey, existing.getClass().getSimpleName(), handler.getClass().getSimpleName());
        } else {
            log.debug("Registered handler for processDefinitionKey '{}': {}",
                    processDefinitionKey, handler.getClass().getSimpleName());
        }
    }

    /**
     * Unregisters a handler by its process definition key.
     *
     * @param processDefinitionKey the process definition key
     * @return the removed handler, or null if not found
     */
    public AbstractProcessStarter<?> unregister(String processDefinitionKey) {
        AbstractProcessStarter<?> removed = handlers.remove(processDefinitionKey);
        if (removed != null) {
            log.info("Unregistered handler for processDefinitionKey '{}'", processDefinitionKey);
        }
        return removed;
    }

    /**
     * Gets a handler by its process definition key.
     *
     * @param processDefinitionKey the process definition key
     * @return Optional containing the handler if found
     */
    public Optional<AbstractProcessStarter<?>> getHandler(String processDefinitionKey) {
        return Optional.ofNullable(handlers.get(processDefinitionKey));
    }

    /**
     * Gets a handler by its process definition key, throwing an exception if not
     * found.
     *
     * @param processDefinitionKey the process definition key
     * @return the handler
     * @throws BusinessException if no handler is found
     */
    @SuppressWarnings("unchecked")
    public <T> AbstractProcessStarter<T> getHandlerOrThrow(String processDefinitionKey) {
        return (AbstractProcessStarter<T>) getHandler(processDefinitionKey)
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.BAD_REQUEST,
                        "Không tìm thấy handler cho processDefinitionKey: " + processDefinitionKey));
    }

    /**
     * Checks if a handler exists for the given process definition key.
     *
     * @param processDefinitionKey the process definition key
     * @return true if a handler exists
     */
    public boolean hasHandler(String processDefinitionKey) {
        return handlers.containsKey(processDefinitionKey);
    }

    /**
     * Gets all registered handlers.
     *
     * @return unmodifiable collection of all handlers
     */
    public Collection<AbstractProcessStarter<?>> getAllHandlers() {
        return Collections.unmodifiableCollection(handlers.values());
    }

    /**
     * Gets all registered process definition keys.
     *
     * @return unmodifiable collection of all keys
     */
    public Collection<String> getAllProcessDefinitionKeys() {
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
