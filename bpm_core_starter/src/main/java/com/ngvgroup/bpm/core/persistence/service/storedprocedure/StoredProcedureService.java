package com.ngvgroup.bpm.core.persistence.service.storedprocedure;

import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureResponse;

import java.util.List;
import java.util.Map;

/**
 * Service interface cho việc gọi stored procedure
 */
public interface StoredProcedureService {
    
    /**
     * Gọi stored procedure với List parameters
     */
    StoredProcedureResponse execute(String procedureName, List<StoredProcedureParameter> parameters);
    
    /**
     * Gọi stored procedure với retry mechanism
     */
    StoredProcedureResponse executeWithRetry(String procedureName, 
                                           Map<String, Object> parameters,
                                           int maxRetries);
    
    /**
     * Kiểm tra kết nối database
     */
    boolean testConnection();
    
    /**
     * Lấy thông tin connection pool
     */
    Map<String, Object> getConnectionPoolInfo();
} 