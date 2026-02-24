package com.ngvgroup.bpm.core.common.util;

import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;

import java.sql.Types;
import java.util.List;
import java.util.Map;

/**
 * Utility class cho stored procedure
 */
public class StoredProcedureUtils {
    
    /**
     * Validate tên stored procedure
     */
    public static void validateProcedureName(String procedureName) {
        if (StringUtils.isEmpty(procedureName)) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Validate parameters
     */
    public static void validateParameters(List<StoredProcedureParameter> parameters) {
        if (parameters == null) {
            return;
        }
        
        for (StoredProcedureParameter param : parameters) {
            if (StringUtils.isEmpty(param.getName())) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
            
            if (param.isOutput() && param.getSqlType() == null) {
                throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }
    }
    
    /**
     * Convert Map parameters thành List StoredProcedureParameter
     */
    public static List<StoredProcedureParameter> convertMapToParameters(Map<String, Object> parameters) {
        if (parameters == null) {
            return List.of();
        }
        
        return parameters.entrySet().stream()
                .map(entry -> StoredProcedureParameter.input(entry.getKey(), entry.getValue()))
                .toList();
    }
    
    /**
     * Lấy SQL type từ Java type
     */
    public static Integer getSqlTypeFromJavaType(Object value) {
        if (value == null) {
            return Types.NULL;
        }
        
        if (value instanceof String) {
            return Types.VARCHAR;
        } else if (value instanceof Integer) {
            return Types.INTEGER;
        } else if (value instanceof Long) {
            return Types.BIGINT;
        } else if (value instanceof Double) {
            return Types.DOUBLE;
        } else if (value instanceof Float) {
            return Types.FLOAT;
        } else if (value instanceof Boolean) {
            return Types.BOOLEAN;
        } else if (value instanceof java.util.Date) {
            return Types.TIMESTAMP;
        } else if (value instanceof java.sql.Date) {
            return Types.DATE;
        } else if (value instanceof java.sql.Time) {
            return Types.TIME;
        } else if (value instanceof java.sql.Timestamp) {
            return Types.TIMESTAMP;
        } else {
            return Types.OTHER;
        }
    }
    
    /**
     * Format procedure name với schema nếu cần
     */
    public static String formatProcedureName(String procedureName, String schema) {
        if (StringUtils.isEmpty(schema)) {
            return procedureName;
        }
        return schema + "." + procedureName;
    }
    
    /**
     * Tạo log message cho stored procedure call
     */
    public static String createLogMessage(String procedureName, List<StoredProcedureParameter> parameters, 
                                        Long executionTime, boolean success) {
        StringBuilder sb = new StringBuilder();
        sb.append("Stored Procedure: ").append(procedureName);
        sb.append(", Success: ").append(success);
        
        if (executionTime != null) {
            sb.append(", Execution Time: ").append(executionTime).append("ms");
        }
        
        if (parameters != null && !parameters.isEmpty()) {
            sb.append(", Parameters: ");
            for (int i = 0; i < parameters.size(); i++) {
                StoredProcedureParameter param = parameters.get(i);
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(param.getName()).append("=");
                if (param.isOutput()) {
                    sb.append("[OUTPUT]");
                } else {
                    sb.append(param.getValue());
                }
            }
        }
        
        return sb.toString();
    }
} 