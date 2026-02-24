package ngvgroup.com.bpm.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a Process Starter handler and specify its
 * process definition key.
 * <p>
 * Classes annotated with this annotation must extend
 * {@link ngvgroup.com.bpm.client.template.AbstractProcessStarter}
 * and will be automatically discovered and registered with
 * {@link ngvgroup.com.bpm.client.template.ProcessStarterRegistry}.
 * <p>
 * Example usage:
 * 
 * <pre>
 * {@code
 * @ProcessStarterSubscription("customer-create-process")
 * public class CustomerCreateProcessStarter extends AbstractProcessStarter<CustomerDto> {
 *     // ...
 * }
 * }
 * </pre>
 *
 * @see ngvgroup.com.bpm.client.template.AbstractProcessStarter
 * @see ngvgroup.com.bpm.client.template.ProcessStarterRegistry
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ProcessStarterSubscription {

    /**
     * The BPMN process definition key (the 'id' attribute of the process element in
     * BPMN).
     * This key must be unique across all processes in the system.
     *
     * @return the process definition key
     */
    String value();
}
