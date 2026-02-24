package ngvgroup.com.bpm.client.template;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.request.DraftTaskBpmRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.response.TaskViewResponse;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.bpm.client.utils.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Service for orchestrating User Task operations.
 * <p>
 * This service uses {@link UserTaskRegistry} to dynamically lookup the
 * appropriate
 * handler based on taskDefinitionKey fetched from Camunda, then delegates the
 * operation
 * to the handler.
 * <p>
 * All methods accept only taskId - the taskDefinitionKey is always resolved
 * from Camunda.
 *
 * @see ngvgroup.com.bpm.client.annotation.UserTaskSubscription
 * @see UserTaskRegistry
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserTaskService {

    private final UserTaskRegistry userTaskRegistry;
    private final BpmFeignClient bpmFeignClient;

    /**
     * Gets task details by fetching taskDefinitionKey from Camunda.
     *
     * @param taskId the Camunda task ID
     * @return TaskViewResponse containing merged BPM and business data
     * @throws BusinessException if no handler is found for the taskDefinitionKey
     */
    public TaskViewResponse<?> getTaskDetail(String taskId) {
        log.debug("Getting task detail for taskId: {}", taskId);

        String taskDefinitionKey = getTaskDefinitionKeyFromCamunda(taskId);
        AbstractUserTask<?> handler = userTaskRegistry.getHandlerOrThrow(taskDefinitionKey);

        return handler.getTaskDetail(taskId);
    }

    /**
     * Submits/completes a task.
     * The taskDefinitionKey is resolved from Camunda using the taskId in the
     * request.
     *
     * @param dto          the submit request containing BPM data and business data
     * @param multiFileMap uploaded files
     */
    @SuppressWarnings("unchecked")
    public <T> void submitTask(SubmitTaskRequest<T> dto,
            MultiValueMap<String, MultipartFile> multiFileMap) {
        String taskId = dto.getBpmData().getTaskId();
        log.debug("Submitting task for taskId: {}", taskId);

        String taskDefinitionKey = getTaskDefinitionKeyFromCamunda(taskId);
        AbstractUserTask<T> handler = userTaskRegistry.getHandlerOrThrow(taskDefinitionKey);

        // Fix type mismatch: Convert Map to BusinessDto if needed
        if (dto.getBusinessData() instanceof Map) {
            Class<T> businessClass = handler.getBusinessClass();
            if (businessClass != null) {
                T convertedData = JsonUtil.convertValue(dto.getBusinessData(),
                        businessClass);
                dto.setBusinessData(convertedData);
            }
        }

        handler.submitTask((SubmitTaskRequest<T>) dto, multiFileMap);

        log.info("Task submitted successfully: taskDefinitionKey={}, taskId={}",
                taskDefinitionKey, taskId);
    }

    /**
     * Saves task as draft.
     * The taskDefinitionKey is resolved from Camunda using the taskId in the
     * request.
     *
     * @param dto          the draft request containing BPM data and business data
     * @param multiFileMap uploaded files
     */
    @SuppressWarnings("unchecked")
    public <T> void saveDraft(DraftTaskBpmRequest<T> dto,
            MultiValueMap<String, MultipartFile> multiFileMap) {
        String taskId = dto.getBpmData().getTaskId();
        log.debug("Saving draft for taskId: {}", taskId);

        String taskDefinitionKey = getTaskDefinitionKeyFromCamunda(taskId);
        AbstractUserTask<T> handler = userTaskRegistry.getHandlerOrThrow(taskDefinitionKey);

        // Fix type mismatch: Convert Map to BusinessDto if needed
        if (dto.getBusinessData() instanceof Map) {
            Class<T> businessClass = handler.getBusinessClass();
            if (businessClass != null) {
                T convertedData = JsonUtil.convertValue(dto.getBusinessData(),
                        businessClass);
                dto.setBusinessData(convertedData);
            }
        }

        handler.saveDraft((DraftTaskBpmRequest<T>) dto, multiFileMap);

        log.info("Draft saved successfully: taskDefinitionKey={}, taskId={}",
                taskDefinitionKey, taskId);
    }

    /**
     * Checks if a handler exists for a given taskId.
     *
     * @param taskId the Camunda task ID
     * @return true if a handler is registered for the task's taskDefinitionKey
     */
    public boolean hasHandler(String taskId) {
        try {
            String taskDefinitionKey = getTaskDefinitionKeyFromCamunda(taskId);
            return userTaskRegistry.hasHandler(taskDefinitionKey);
        } catch (Exception e) {
            log.warn("Failed to check handler for taskId: {}", taskId, e);
            return false;
        }
    }

    /**
     * Gets the task definition key from Camunda task metadata.
     *
     * @param taskId the task ID
     * @return the task definition key
     * @throws BusinessException if taskDefinitionKey cannot be determined
     */
    private String getTaskDefinitionKeyFromCamunda(String taskId) {
        try {
            return bpmFeignClient.getTaskDefinitionKeyFromCamunda(taskId).getData();
        } catch (Exception e) {
            log.error("Failed to get taskDefinitionKey for taskId: {}", taskId, e);
            throw new BusinessException(ErrorCode.BAD_REQUEST,
                    "Không thể xác định taskDefinitionKey cho taskId: " + taskId);
        }
    }
}
