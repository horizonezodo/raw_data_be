package ngvgroup.com.bpm.core.utils;

import java.io.InputStream;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.VariableScope;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class VariablesUtil {
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature());
        // Recommend: Ignore properties in JSON that are not in your Java Class to
        // prevent errors
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private VariablesUtil() {
    }

    public static <T> T getData(DelegateExecution execution, String variableKey, Class<T> clazz) {
        return getValueFromScope(execution, variableKey, clazz);
    }

    public static <T> T getData(DelegateTask task, String variableKey, Class<T> clazz) {
        return getValueFromScope(task, variableKey, clazz);
    }

    // --- Helper Methods ---

    private static <T> T getValueFromScope(VariableScope scope, String variableKey, Class<T> clazz) {
        TypedValue typedValue = scope.getVariableTyped(variableKey, false);
        if (typedValue == null) {
            return null;
        }

        Object valueToParse;
        if (typedValue instanceof ObjectValue objectValue) {
            // Try to get the serialized string first
            valueToParse = objectValue.getValueSerialized();
            // If serialized value is null, fallback to the deserialized object
            if (valueToParse == null) {
                valueToParse = objectValue.getValue();
            }
        } else {
            valueToParse = typedValue.getValue();
        }

        return parseObject(valueToParse, variableKey, clazz);
    }

    /**
     * Parse object to target class
     */
    private static <T> T parseObject(Object rawValue, String variableKey, Class<T> clazz) {
        if (rawValue == null) {
            return null;
        }
        try {
            // 1. If rawValue is ALREADY the correct class, just cast and return
            if (clazz.isInstance(rawValue)) {
                return clazz.cast(rawValue);
            }

            // 2. Handle InputStream (Files)
            if (rawValue instanceof InputStream inputStream) {
                return objectMapper.readValue(inputStream, clazz);
            }

            // 3. Handle String (JSON String)
            if (rawValue instanceof String str) {
                return objectMapper.readValue(str, clazz);
            }

            // 4. Handle Camunda Spin Objects (JacksonJsonNode, etc.)
            // Spin objects are wrappers, but their toString() returns the JSON string.
            if (rawValue.getClass().getName().startsWith("org.camunda.spin")) {
                return objectMapper.readValue(rawValue.toString(), clazz);
            }

            // 5. Handle other Objects (LinkedHashMap, etc.)
            // USE convertValue, NOT readValue(toString())
            return objectMapper.convertValue(rawValue, clazz);

        } catch (Exception e) {
            log.error("Error parsing variable [{}]. Type: {}. Error: {}",
                    variableKey,
                    rawValue.getClass().getName(),
                    e.getMessage(),
                    e);

            throw new BusinessException(ErrorCode.BAD_REQUEST, "Không có dữ liệu %s", variableKey);
        }
    }
}