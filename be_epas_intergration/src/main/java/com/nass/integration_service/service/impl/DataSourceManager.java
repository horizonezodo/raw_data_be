package com.nass.integration_service.service.impl;

import com.nass.integration_service.dto.ComCfgParameterDto;
import com.ngvgroup.bpm.core.config.StoredProcedureProperties;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class DataSourceManager {
    private final StoredProcedureProperties properties;
    private final Map<String, HikariDataSource> dataSourceCache = new ConcurrentHashMap<>();

    private void createConnectionPool(ComCfgParameterDto parameterDto) {
        StoredProcedureProperties.ConnectionPool connectionPool = properties.getConnectionPool();

        HikariConfig config = new HikariConfig();
        var jdbcUrl = parameterDto.getParamValue() == null ? parameterDto.getParamDefaultValue() : parameterDto.getParamValue();
        config.setJdbcUrl(jdbcUrl);

        if (jdbcUrl.contains("oracle")) {
            config.setDriverClassName("oracle.jdbc.OracleDriver");
        } else if (jdbcUrl.contains("sqlserver")) {
            config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } else if (jdbcUrl.contains("postgresql")) {
            config.setDriverClassName("org.postgresql.Driver");
        } else if (jdbcUrl.contains("mysql")) {
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        }

        config.setMaximumPoolSize(connectionPool.getMaxSize());
        config.setMinimumIdle(connectionPool.getMinIdle());
        config.setConnectionTimeout(connectionPool.getConnectionTimeout());
        config.setIdleTimeout(connectionPool.getIdleTimeout());
        config.setMaxLifetime(connectionPool.getMaxLifetime());
        HikariDataSource dataSource = new HikariDataSource(config);
        dataSourceCache.put(parameterDto.getParamCode(), dataSource);
    }

    public Connection getConnection(ComCfgParameterDto parameter) {
        String paramCode = parameter.getParamCode();
        if (!dataSourceCache.containsKey(paramCode)) {
            createConnectionPool(parameter);
        }

        DataSource dataSource = dataSourceCache.get(paramCode);
        if (dataSource == null) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Không thể tạo pool cho user: " + paramCode);
        }

        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            log.error("Lỗi khi lấy connection: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
} 