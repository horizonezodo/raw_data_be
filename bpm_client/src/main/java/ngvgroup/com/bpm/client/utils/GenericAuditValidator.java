package ngvgroup.com.bpm.client.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.dto.shared.AuditDto;
import ngvgroup.com.bpm.client.exception.BpmClientErrorCode;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.*;

@Slf4j
public class GenericAuditValidator {

    // ObjectMapper bỏ qua null fields khi serialize - dùng để so sánh DTO với Map
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .setDateFormat(new java.text.SimpleDateFormat("dd/MM/yyyy"))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    private static final ExpressionParser SPEL_PARSER = new SpelExpressionParser();

    /**
     * Entry point để validate audit data với object.
     * Sử dụng SpEL để resolve fieldCode và compare với newValue/oldValue.
     *
     * @param rootDto object gốc chứa data
     * @param audits  danh sách audit cần validate
     */
    @SuppressWarnings("unchecked")
    public static void validate(Object rootDto, List<AuditDto> audits) {
        if (rootDto == null || audits == null || audits.isEmpty()) {
            return;
        }

        // Convert rootDto sang Map<String, Object> trước khi validate
        Map<String, Object> rootMap;
        if (rootDto instanceof Map) {
            rootMap = (Map<String, Object>) rootDto;
        } else {
            rootMap = OBJECT_MAPPER.convertValue(rootDto, Map.class);
        }

        for (AuditDto audit : audits) {
            validateSingleAudit(rootMap, audit);
        }
    }

    /**
     * Validate một audit record.
     * Dùng SpEL để lấy giá trị từ rootDto theo fieldCode.
     */
    private static void validateSingleAudit(Object rootDto, AuditDto audit) {
        String fieldCode = audit.getFieldCode();
        Object newValue = audit.getNewValue();
        Object oldValue = audit.getOldValue();

        // Resolve value từ rootDto bằng SpEL
        Object profileData = resolveSpelExpression(rootDto, fieldCode);

        // Validate
        validateValue(profileData, oldValue, newValue, fieldCode);
    }

    /**
     * Resolve SpEL expression để lấy object từ Map.
     * Convert fieldCode sang bracket notation cho Map access.
     * Ví dụ:
     * - "customerName" -> "['customerName']"
     * - "extendedInfo.isPoorHousehold" -> "['extendedInfo']['isPoorHousehold']"
     * - "relations[0]" -> "['relations'][0]"
     * - "addressInfo[0].provinceCode" -> "['addressInfo'][0]['provinceCode']"
     */
    private static Object resolveSpelExpression(Object rootDto, String expression) {
        try {
            // Convert dot notation to bracket notation for Map access
            String mapExpression = convertToMapExpression(expression);
            StandardEvaluationContext context = new StandardEvaluationContext(rootDto);
            return SPEL_PARSER.parseExpression(mapExpression).getValue(context);
        } catch (Exception e) {
            log.debug("SpEL expression '{}' returned null: {}", expression, e.getMessage());
            return null;
        }
    }

    /**
     * Convert fieldCode thành bracket notation cho Map access.
     * "name" -> "['name']"
     * "a.b" -> "['a']['b']"
     * "a[0].b" -> "['a'][0]['b']"
     */
    private static String convertToMapExpression(String fieldCode) {
        StringBuilder result = new StringBuilder();
        StringBuilder currentKey = new StringBuilder();

        for (int i = 0; i < fieldCode.length(); i++) {
            char c = fieldCode.charAt(i);

            if (c == '.') {
                // Flush current key as bracket notation
                if (currentKey.length() > 0) {
                    result.append("['").append(currentKey).append("']");
                    currentKey.setLength(0);
                }
            } else if (c == '[') {
                // Flush current key, then append array index
                if (currentKey.length() > 0) {
                    result.append("['").append(currentKey).append("']");
                    currentKey.setLength(0);
                }
                // Find closing bracket and append index
                int closeBracket = fieldCode.indexOf(']', i);
                if (closeBracket > i) {
                    result.append(fieldCode, i, closeBracket + 1);
                    i = closeBracket;
                }
            } else {
                currentKey.append(c);
            }
        }

        // Flush remaining key
        if (currentKey.length() > 0) {
            result.append("['").append(currentKey).append("']");
        }

        return result.toString();
    }

    /**
     * Validate giá trị profile với audit data.
     */
    private static void validateValue(Object profileData, Object oldValue, Object newValue, String fieldCode) {
        // Case: Xóa field (newValue empty) -> profile phải null hoặc empty
        if (isEmptyValue(newValue)) {
            if (!isEmptyValue(oldValue) && !isEmptyValue(profileData)) {
                String errorMessage = String.format("DELETE_FAILED: Field='%s' | Expected null/empty but got '%s'",
                        fieldCode, profileData);
                log.error(errorMessage);
                throw new BusinessException(BpmClientErrorCode.INVALID_AUDIT_DATA, errorMessage);
            }
            return;
        }

        // Case: Thêm/Sửa field -> profile phải match newValue
        if (!valueMatches(profileData, newValue)) {
            String errorMessage = String.format("MISMATCH: Field='%s' | Expected='%s' | Actual='%s'",
                    fieldCode, newValue, profileData);
            log.error(errorMessage);
            throw new BusinessException(BpmClientErrorCode.INVALID_AUDIT_DATA, errorMessage);
        }
    }

    /**
     * So sánh giá trị Object: hỗ trợ DTO vs Map.
     * Thực hiện "subset" comparison - chỉ check các field trong auditValue
     * có tồn tại và match trong element, bỏ qua extra fields trong element.
     */
    private static boolean valueMatches(Object element, Object auditValue) {
        if (element == null && auditValue == null) {
            return true;
        }
        if (element == null || auditValue == null) {
            return false;
        }

        // So sánh trực tiếp (cho primitive types)
        if (Objects.equals(element, auditValue)) {
            return true;
        }

        // So sánh sau khi normalize (cho strings, dates)
        String normalizedElement = normalizeValue(element);
        String normalizedAudit = normalizeValue(auditValue);
        if (normalizedElement != null && normalizedElement.equals(normalizedAudit)) {
            return true;
        }

        // Với complex objects - thực hiện subset comparison
        try {
            JsonNode actualNode = OBJECT_MAPPER.valueToTree(element);
            JsonNode expectedNode = OBJECT_MAPPER.valueToTree(auditValue);
            return containsAllFields(actualNode, expectedNode);
        } catch (Exception e) {
            log.warn("Failed to compare objects: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Normalize value để so sánh - handle date formats và strings.
     */
    private static String normalizeValue(Object value) {
        if (value == null) {
            return null;
        }
        String str = value.toString();
        // Normalize date format: convert dd/MM/yyyy to yyyy-MM-dd for comparison
        if (str.matches("\\d{2}/\\d{2}/\\d{4}")) {
            String[] parts = str.split("/");
            return parts[2] + "-" + parts[1] + "-" + parts[0];
        }
        return str;
    }

    /**
     * Check xem actualNode có chứa tất cả fields trong expectedNode không.
     * Bỏ qua các extra fields trong actualNode.
     * Bỏ qua empty strings trong actualNode nếu expectedNode không có field đó.
     */
    private static boolean containsAllFields(JsonNode actualNode, JsonNode expectedNode) {
        if (expectedNode.isObject()) {
            if (!actualNode.isObject()) {
                return false;
            }
            Iterator<Map.Entry<String, JsonNode>> fields = expectedNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> expectedField = fields.next();
                String fieldName = expectedField.getKey();
                JsonNode expectedValue = expectedField.getValue();
                JsonNode actualValue = actualNode.get(fieldName);

                if (actualValue == null || actualValue.isNull()) {
                    // Field không tồn tại trong actual nhưng expected có
                    if (!expectedValue.isNull()) {
                        return false;
                    }
                } else if (!containsAllFields(actualValue, expectedValue)) {
                    return false;
                }
            }
            return true;
        } else if (expectedNode.isArray()) {
            if (!actualNode.isArray() || actualNode.size() != expectedNode.size()) {
                return false;
            }
            for (int i = 0; i < expectedNode.size(); i++) {
                if (!containsAllFields(actualNode.get(i), expectedNode.get(i))) {
                    return false;
                }
            }
            return true;
        } else {
            // Primitive value - compare sau khi normalize
            String actualStr = normalizeValue(actualNode.asText());
            String expectedStr = normalizeValue(expectedNode.asText());
            return Objects.equals(actualStr, expectedStr);
        }
    }

    private static boolean isEmptyValue(Object val) {
        if (val == null) {
            return true;
        }
        if (val instanceof String s) {
            return s.trim().isEmpty();
        }
        if (val instanceof Collection<?> c) {
            return c.isEmpty();
        }
        if (val instanceof Map<?, ?> m) {
            return m.isEmpty();
        }
        return false;
    }
}
