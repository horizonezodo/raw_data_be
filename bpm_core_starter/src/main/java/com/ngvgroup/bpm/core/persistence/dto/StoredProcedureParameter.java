package com.ngvgroup.bpm.core.persistence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO định nghĩa parameter cho stored procedure
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoredProcedureParameter {
    
    /**
     * Tên parameter
     */
    private String name;
    
    /**
     * Giá trị parameter
     */
    private Object value;
    
    /**
     * Kiểu dữ liệu của parameter
     */
    private Integer sqlType;
    
    /**
     * Có phải là output parameter không
     */
    @Builder.Default
    private boolean output = false;

    /**
     * Factory method cho input parameter
     * @param name tên parameter
     * @param value giá trị parameter
     * @return StoredProcedureParameter object
     */
    public static StoredProcedureParameter input(String name, Object value) {
        return StoredProcedureParameter.builder()
                .name(name)
                .value(value)
                .output(false)
                .build();
    }

    /**
     * Factory method cho output parameter
     * @param name tên parameter
     * @param sqlType kiểu dữ liệu SQL
     * @return StoredProcedureParameter object
     */
    public static StoredProcedureParameter output(String name, Integer sqlType) {
        return StoredProcedureParameter.builder()
                .name(name)
                .sqlType(sqlType)
                .output(true)
                .build();
    }
} 