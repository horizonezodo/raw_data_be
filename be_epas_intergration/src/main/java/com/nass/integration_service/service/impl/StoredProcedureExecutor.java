package com.nass.integration_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nass.integration_service.dto.ComCfgParameterDto;
import com.ngvgroup.bpm.core.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.dto.StoredProcedureResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;

@Component
@RequiredArgsConstructor
public class StoredProcedureExecutor {
    private final DataSourceManager dataSourceManager;

    public StoredProcedureResponse executeStoredProcedure(String procedureName, List<StoredProcedureParameter> parameters, ComCfgParameterDto parameterDto, int queryTimeoutSeconds) throws SQLException {
        StoredProcedureResponse response;
        try (
                Connection connection = dataSourceManager.getConnection(parameterDto);
                CallableStatement stmt = this.createCallableStatement(connection, procedureName, parameters, queryTimeoutSeconds);
        ) {
            this.setParameters(stmt, parameters);
            boolean hasResultSet = stmt.execute();
            List<Map<String, Object>> resultSet = new ArrayList<>();
            if (hasResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    resultSet = this.convertResultSetToList(rs);
                }
            }
            Map<String, Object> outputParameters = this.getOutputParameters(stmt, parameters);
            response = StoredProcedureResponse.success(resultSet, outputParameters);
        }
        return response;
    }

    private CallableStatement createCallableStatement(Connection connection, String procedureName, List<StoredProcedureParameter> parameters, int queryTimeoutSeconds) throws SQLException {
        String sql = this.buildCallableStatementSql(procedureName, parameters);
        CallableStatement stmt = connection.prepareCall(sql);
        stmt.setQueryTimeout(queryTimeoutSeconds);
        return stmt;
    }

    private String buildCallableStatementSql(String procedureName, List<StoredProcedureParameter> parameters) {
        StringBuilder sql = new StringBuilder("{call ");
        sql.append(procedureName).append("(");
        if (parameters != null && !parameters.isEmpty()) {
            for (int i = 0; i < parameters.size(); ++i) {
                if (i > 0) {
                    sql.append(", ");
                }
                sql.append("?");
            }
        }
        sql.append(")}");
        return sql.toString();
    }

    private void setParameters(CallableStatement stmt, List<StoredProcedureParameter> parameters) throws SQLException {
        if (parameters != null) {
            for (int i = 0; i < parameters.size(); ++i) {
                StoredProcedureParameter param = parameters.get(i);
                int paramIndex = i + 1;
                // Xử lý UDTT: nếu value là List<ParamInput> thì convert sang SQLServerDataTable
                if (param.getSqlType() != null && param.getSqlType() == java.sql.Types.STRUCT) {
                    Object value = param.getValue();
                    SQLServerDataTable table = null;
                    if (value instanceof SQLServerDataTable) {
                        table = (SQLServerDataTable) value;
                    } else if (value instanceof java.util.List) {
                        try {
                            java.util.List<?> rawList = (java.util.List<?>) value;
                            java.util.List<com.nass.integration_service.dto.ParamInput> paramInputs = new java.util.ArrayList<>();
                            for (Object item : rawList) {
                                if (item instanceof com.nass.integration_service.dto.ParamInput) {
                                    paramInputs.add((com.nass.integration_service.dto.ParamInput) item);
                                } else if (item instanceof java.util.Map) {
                                    java.util.Map map = (java.util.Map) item;
                                    com.nass.integration_service.dto.ParamInput pi = new com.nass.integration_service.dto.ParamInput();
                                    pi.setParamName((String) map.get("paramName"));
                                    pi.setParamType((String) map.get("paramType"));

                                    Object rawValue = map.get("paramValue");
                                    if (rawValue instanceof String) {
                                        pi.setParamValue((String) rawValue);
                                    } else {
                                        // Convert object thành chuỗi JSON
                                        ObjectMapper mapper = new ObjectMapper();
                                        pi.setParamValue(mapper.writeValueAsString(rawValue));
                                    }
                                    paramInputs.add(pi);
                                }
                            }
                            table = com.nass.integration_service.dto.ParamInputUtil.toSQLServerDataTable(paramInputs);
                        } catch (Exception e) {
                            throw new SQLException("Lỗi khi convert List<ParamInput> sang SQLServerDataTable", e);
                        }
                    }
                    if (table != null) {
                        SQLServerCallableStatement sqlServerStmt = stmt.unwrap(SQLServerCallableStatement.class);
                        sqlServerStmt.setStructured(paramIndex, "dbo.PARAM_INPUTS", table);
                        continue;
                    }
                }
                if (param.isOutput()) {
                    stmt.registerOutParameter(paramIndex, param.getSqlType());
                } else if (param.getSqlType() != null) {
                    stmt.setObject(paramIndex, param.getValue(), param.getSqlType());
                } else {
                    stmt.setObject(paramIndex, param.getValue());
                }
            }
        }
    }

    private List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; ++i) {
                String columnName = metaData.getColumnName(i);
                int columnType = metaData.getColumnType(i);
                Object value;
                switch (columnType) {
                    case 91:
                        value = rs.getDate(i);
                        break;
                    case 92:
                    case 2013:
                        value = rs.getTime(i);
                        break;
                    case 93:
                    case 2014:
                        value = rs.getTimestamp(i);
                        break;
                    default:
                        value = rs.getObject(i);
                }
                row.put(columnName, value);
            }
            result.add(row);
        }
        return result;
    }

    private Map<String, Object> getOutputParameters(CallableStatement stmt, List<StoredProcedureParameter> parameters) throws SQLException {
        Map<String, Object> outputParams = new HashMap<>();
        if (parameters == null) {
            return outputParams;
        } else {
            for (int i = 0; i < parameters.size(); ++i) {
                StoredProcedureParameter param = parameters.get(i);
                if (param.isOutput()) {
                    Object value = stmt.getObject(i + 1);
                    if (value instanceof ResultSet) {
                        try (ResultSet rs = (ResultSet) value) {
                            outputParams.put(param.getName(), this.convertResultSetToList(rs));
                        }
                    } else {
                        outputParams.put(param.getName(), value);
                    }
                }
            }
            return outputParams;
        }
    }
} 