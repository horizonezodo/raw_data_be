package ngvgroup.com.bpm.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a User Task handler and specify its task
 * definition key.
 * <p>
 * Classes annotated with this annotation must extend
 * {@link ngvgroup.com.bpm.client.template.AbstractUserTask}
 * and will be automatically discovered and registered with
 * {@link ngvgroup.com.bpm.client.template.UserTaskRegistry}.
 * <p>
 * Example usage:
 * 
 * <pre>
 * {@code
 * @UserTaskSubscription("customer-approval-task")
 * public class CustomerApprovalTask extends AbstractUserTask<CustomerDto> {
 *     // ...
 * }
 * }
 * </pre>
 *
 * @see ngvgroup.com.bpm.client.template.AbstractUserTask
 * @see ngvgroup.com.bpm.client.template.UserTaskRegistry
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface UserTaskSubscription {

    /**
     * The BPMN task definition key (the 'id' attribute of the userTask element in
     * BPMN).
     * This key must be unique across all processes in the system.
     *
     * @return the task definition key
     */
    String value();
}
