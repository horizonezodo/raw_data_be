package com.ngvgroup.bpm.core.persistence.service.storedprocedure.impl;

import com.ngvgroup.bpm.core.persistence.config.StoredProcedureProperties;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.persistence.dto.StoredProcedureResponse;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.StoredProcedureService;
import com.ngvgroup.bpm.core.common.util.StoredProcedureUtils;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation của StoredProcedureService
 */
@Slf4j
@Service
@ConditionalOnProperty(name = "bpm.core.stored-procedure.enabled", havingValue = "true", matchIfMissing = true)
public class StoredProcedureServiceImpl implements StoredProcedureService {
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private StoredProcedureProperties properties;
    
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    @Override
    public StoredProcedureResponse execute(String procedureName, List<StoredProcedureParameter> parameters) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            // Validate input
            StoredProcedureUtils.validateProcedureName(procedureName);
            StoredProcedureUtils.validateParameters(parameters);
            
            // Log request
            if (properties.getLogging().isEnabled()) {
                log.info("Executing stored procedure: {}", procedureName);
                if (properties.getLogging().isLogParameters()) {
                    log.debug("Parameters: {}", parameters);
                }
            }
            
            // Execute stored procedure
            StoredProcedureResponse response = executeStoredProcedure(procedureName, parameters);
            success = true;
            
            // Log response
            if (properties.getLogging().isEnabled()) {
                long executionTime = System.currentTimeMillis() - startTime;
                String logMessage = StoredProcedureUtils.createLogMessage(
                    procedureName, 
                    parameters, 
                    executionTime, 
                    success
                );
                log.info(logMessage);
            }
            
            return response;
            
        } catch (Exception e) {
            success = false;
            long executionTime = System.currentTimeMillis() - startTime;
            
            if (properties.getLogging().isEnabled()) {
                String logMessage = StoredProcedureUtils.createLogMessage(
                    procedureName, 
                    parameters, 
                    executionTime, 
                    success
                );
                log.error(logMessage + ", Error: " + e.getMessage(), e);
            }
            
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
    
    @Override
    public StoredProcedureResponse executeWithRetry(String procedureName, 
                                                  Map<String, Object> parameters,
                                                  int maxRetries) {
        int attempts = 0;
        Exception lastException = null;
        
        while (attempts < maxRetries) {
            try {
                List<StoredProcedureParameter> paramList = StoredProcedureUtils.convertMapToParameters(parameters);
                return execute(procedureName, paramList);
            } catch (Exception e) {
                lastException = e;
                attempts++;
                
                if (attempts < maxRetries) {
                    try {
                        Thread.sleep(properties.getRetry().getBackoffDelay());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }
        
        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
    
    @Override
    public boolean testConnection() {
        try (Connection connection = dataSource.getConnection()) {
            return connection.isValid(5);
        } catch (SQLException e) {
            log.error("Lỗi khi test connection: {}", e.getMessage());
            return false;
        }
    }
    
    @Override
    public Map<String, Object> getConnectionPoolInfo() {
        Map<String, Object> info = new HashMap<>();
        
        if (dataSource instanceof HikariDataSource) {
            HikariDataSource hikariDataSource = (HikariDataSource) dataSource;
            info.put("activeConnections", hikariDataSource.getHikariPoolMXBean().getActiveConnections());
            info.put("idleConnections", hikariDataSource.getHikariPoolMXBean().getIdleConnections());
            info.put("totalConnections", hikariDataSource.getHikariPoolMXBean().getTotalConnections());
            info.put("threadsAwaitingConnection", hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        
        return info;
    }
    
    /**
     * Thực thi stored procedure
     */
    private StoredProcedureResponse executeStoredProcedure(String procedureName, List<StoredProcedureParameter> parameters) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = createCallableStatement(connection, procedureName, parameters)) {
            
            // Set parameters
            setParameters(stmt, parameters);
            
            // Execute
            boolean hasResultSet = stmt.execute();
            
            // Process results
            List<Map<String, Object>> resultSet = new ArrayList<>();
            Map<String, Object> outputParameters = new HashMap<>();
            
            // Process result sets
            if (hasResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    resultSet = convertResultSetToList(rs);
                }
            }
            
            // Process output parameters
            outputParameters = getOutputParameters(stmt, parameters);
            
            return StoredProcedureResponse.success(resultSet, outputParameters);
        }
    }
    
    /**
     * Tạo CallableStatement
     */
    private CallableStatement createCallableStatement(Connection connection, String procedureName, List<StoredProcedureParameter> parameters) 
            throws SQLException {
        String sql = buildCallableStatementSql(procedureName, parameters);
        CallableStatement stmt = connection.prepareCall(sql);
        
        // Set default timeout from properties
        stmt.setQueryTimeout((int) (properties.getTimeout().getQuery() / 1000)); // Convert to seconds
        
        return stmt;
    }
    
    /**
     * Build SQL cho CallableStatement
     */
    private String buildCallableStatementSql(String procedureName, List<StoredProcedureParameter> parameters) {
        StringBuilder sql = new StringBuilder("{call ");
        sql.append(procedureName).append("(");
        
        if (parameters != null && !parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); i++) {
                if (i > 0) {
                    sql.append(", ");
                }
                sql.append("?");
            }
        }
        
        sql.append(")}");
        return sql.toString();
    }
    
    /**
     * Set parameters cho CallableStatement
     */
    private void setParameters(CallableStatement stmt, List<StoredProcedureParameter> parameters) 
            throws SQLException {
        if (parameters == null) {
            return;
        }
        
        for (int i = 0; i < parameters.size(); i++) {
            StoredProcedureParameter param = parameters.get(i);
            int paramIndex = i + 1;
            
            if (param.isOutput()) {
                stmt.registerOutParameter(paramIndex, param.getSqlType());
            } else {
                if (param.getSqlType() != null) {
                    stmt.setObject(paramIndex, param.getValue(), param.getSqlType());
                } else {
                    stmt.setObject(paramIndex, param.getValue());
                }
            }
        }
    }
    
    /**
     * Convert ResultSet thành List<Map>
     */
    private List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value;
                int columnType = metaData.getColumnType(i);

                // Convert specific SQL types to standard Java types that Jackson can serialize
                switch (columnType) {
                    case Types.TIMESTAMP:
                    case Types.TIMESTAMP_WITH_TIMEZONE:
                        value = rs.getTimestamp(i);
                        break;
                    case Types.DATE:
                        value = rs.getDate(i);
                        break;
                    case Types.TIME:
                    case Types.TIME_WITH_TIMEZONE:
                        value = rs.getTime(i);
                        break;
                    default:
                        value = rs.getObject(i);
                        break;
                }
                row.put(columnName, value);
            }
            result.add(row);
        }
        return result;
    }
    
    /**
     * Lấy output parameters (hỗ trợ nhiều SYS_REFCURSOR)
     */
    private Map<String, Object> getOutputParameters(CallableStatement stmt, List<StoredProcedureParameter> parameters)
            throws SQLException {
        Map<String, Object> outputParams = new HashMap<>();

        if (parameters == null) {
            return outputParams;
        }

        for (int i = 0; i < parameters.size(); i++) {
            StoredProcedureParameter param = parameters.get(i);
            if (param.isOutput()) {
                Object value = stmt.getObject(i + 1);
                // Kiểm tra nếu là SYS_REFCURSOR hoặc ResultSet
                if (value instanceof ResultSet) {
                    try (ResultSet rs = (ResultSet) value) {
                        outputParams.put(param.getName(), convertResultSetToList(rs));
                    }
                } else {
                    outputParams.put(param.getName(), value);
                }
            }
        }

        return outputParams;
    }
} 