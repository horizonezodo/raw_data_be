package com.ngvgroup.bpm.core.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO chứa kết quả trả về từ stored procedure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoredProcedureResponse {
    
    /**
     * Kết quả thành công hay không
     */
    @Builder.Default
    private boolean success = true;
    
    /**
     * Thông báo lỗi nếu có
     */
    private String errorMessage;
    
    /**
     * Mã lỗi nếu có
     */
    private String errorCode;
    
    /**
     * Dữ liệu trả về từ stored procedure
     */
    private List<Map<String, Object>> resultSet;
    
    /**
     * Output parameters
     */
    private Map<String, Object> outputParameters;
    
    /**
     * Thời gian thực thi (milliseconds)
     */
    private Long executionTime;
    
    /**
     * Số dòng bị ảnh hưởng
     */
    private Integer affectedRows;
    
    /**
     * Constructor cho response thành công
     */
    public static StoredProcedureResponse success(List<Map<String, Object>> resultSet) {
        return StoredProcedureResponse.builder()
                .success(true)
                .resultSet(resultSet)
                .build();
    }
    
    /**
     * Constructor cho response thành công với output parameters
     */
    public static StoredProcedureResponse success(List<Map<String, Object>> resultSet, 
                                                 Map<String, Object> outputParameters) {
        return StoredProcedureResponse.builder()
                .success(true)
                .resultSet(resultSet)
                .outputParameters(outputParameters)
                .build();
    }
    
    /**
     * Constructor cho response lỗi
     */
    public static StoredProcedureResponse error(String errorMessage) {
        return StoredProcedureResponse.builder()
                .success(false)
                .errorMessage(errorMessage)
                .build();
    }
    
    /**
     * Constructor cho response lỗi với error code
     */
    public static StoredProcedureResponse error(String errorCode, String errorMessage) {
        return StoredProcedureResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .build();
    }
} 