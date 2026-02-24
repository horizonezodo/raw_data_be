package ngvgroup.com.bpm.client.utils;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.bpm.client.dto.variable.TaskBpmData;
import ngvgroup.com.bpm.client.dto.camunda.CamundaVariableDto;
import ngvgroup.com.bpm.client.constant.VariableConstants;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.client.task.ExternalTask;

/**
 * Utility class for extracting, converting, and mapping Camunda process
 * variables.
 * Provides centralized methods to safely extract typed data from ExternalTask
 * variables and convert business data to Camunda variable format.
 */
@Slf4j
public class CamundaVariablesUtil {

    private static final String STRING = "String";

    private CamundaVariablesUtil() {
    }

    // ===============================
    // READING VARIABLES (from ExternalTask)
    // ===============================

    /**
     * Get Process Data variable
     */
    public static ProcessData getProcessData(ExternalTask externalTask) {
        return getTypedVariable(externalTask, VariableConstants.PROCESS_DATA_VARIABLE, ProcessData.class);
    }

    /**
     * Get Task BPM Data variable
     */
    public static TaskBpmData getTaskBpmData(ExternalTask externalTask) {
        return getTypedVariable(externalTask, VariableConstants.TASK_BPM_DATA_VARIABLE, TaskBpmData.class);
    }

    /**
     * Get approval result variable
     * Values: APPROVE, REJECT, CANCEL, COMPLETE, SEND_APPROVE
     */
    public static String getApprovalResult(ExternalTask externalTask) {
        Object value = externalTask.getVariable(VariableConstants.APPROVAL_RESULT_VARIABLE);
        return value != null ? value.toString() : null;
    }

    public static String getTenantId(ExternalTask externalTask) {
        Object value = externalTask.getVariable(VariableConstants.TENANT_ID_VARIABLE);
        return value != null ? value.toString() : null;
    }

    /**
     * Get generic typed variable from ExternalTask
     * Safely converts JSON string to target class
     *
     * @param externalTask The Camunda external task
     * @param variableName The variable name to extract
     * @param targetClass  The target class to convert to
     * @param <T>          Generic type parameter
     * @return Converted object or null if extraction fails
     */
    public static <T> T getTypedVariable(ExternalTask externalTask, String variableName, Class<T> targetClass) {
        try {
            Object data = externalTask.getVariable(variableName);
            if (data == null) {
                log.warn("Variable '{}' not found in external task", variableName);
                return null;
            }

            // If data is already of target type, return as is
            if (targetClass.isInstance(data)) {
                return targetClass.cast(data);
            }

            // Fix: Handle InputStream (from File variables - backward compatibility)
            if (data instanceof InputStream) {
                return JsonUtil.parseJson((InputStream) data, targetClass);
            }

            // Handle String (typically JSON variables when fetched without deserialization
            // or raw String variables)
            if (data instanceof String) {
                return JsonUtil.parseJson((String) data, targetClass);
            }

            // Fallback: Try to convert any other object (e.g., LinkedHashMap from
            // Spin/Jackson) to target class via JSON
            // This handles cases where Camunda client/Spin might have already partially
            // deserialized it to a Map
            String jsonString = JsonUtil.toJsonString(data);
            return JsonUtil.parseJson(jsonString, targetClass);

        } catch (BusinessException e) {
            log.error("Failed to parse JSON for variable '{}' to class {}", variableName, targetClass.getSimpleName(),
                    e);
        } catch (Exception e) {
            log.error("Unexpected error extracting variable '{}' as {}", variableName, targetClass.getSimpleName(), e);
        }

        return null;
    }

    /**
     * Get generic typed variable from CamundaVariableDto
     * 
     * @param variableDto The Camunda variable DTO
     * @param targetClass The target class to convert to
     * @param <T>         Generic type parameter
     * @return Converted object or null
     */
    public static <T> T getTypedVariable(CamundaVariableDto variableDto, Class<T> targetClass) {
        if (variableDto == null || variableDto.getValue() == null) {
            return null;
        }

        Object data = variableDto.getValue();

        // If data is already of target type, return as is
        if (targetClass.isInstance(data)) {
            return targetClass.cast(data);
        }

        // Handle String (typically JSON variables)
        if (data instanceof String) {
            return JsonUtil.parseJson((String) data, targetClass);
        }

        // Fallback: Try to convert via JSON
        String jsonString = JsonUtil.toJsonString(data);
        return JsonUtil.parseJson(jsonString, targetClass);
    }

    // ===============================
    // WRITING VARIABLES (to Camunda)
    // ===============================

    /**
     * Convert Map<String, Object> nghiệp vụ sang Map<String, CamundaVariableDto>
     */
    public static Map<String, CamundaVariableDto> toCamundaMap(Map<String, Object> sourceMap) {
        Map<String, CamundaVariableDto> targetMap = new HashMap<>();

        if (sourceMap == null || sourceMap.isEmpty()) {
            return targetMap;
        }

        for (Map.Entry<String, Object> entry : sourceMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            if (value == null) {
                targetMap.put(key, new CamundaVariableDto(null, STRING, null));
                continue;
            }

            try {
                CamundaVariableDto variableDto = createCamundaVariable(value);
                targetMap.put(key, variableDto);
            } catch (BusinessException e) {
                log.error("Lỗi khi serialize biến [{}] sang JSON: {}", key, e.getMessage());
            }
        }

        return targetMap;
    }

    /**
     * Logic detect type và tạo DTO
     */
    private static CamundaVariableDto createCamundaVariable(Object value) {

        if (value instanceof String)
            return new CamundaVariableDto(value, STRING, null);
        if (value instanceof Integer)
            return new CamundaVariableDto(value, "Integer", null);
        if (value instanceof Long)
            return new CamundaVariableDto(value, "Long", null);
        if (value instanceof Double)
            return new CamundaVariableDto(value, "Double", null);
        if (value instanceof Boolean)
            return new CamundaVariableDto(value, "Boolean", null);

        // Serialize object to JSON
        String json = JsonUtil.toJsonString(value);

        // Use "Json" type for objects
        return new CamundaVariableDto(json, "Json", null);
    }
}
