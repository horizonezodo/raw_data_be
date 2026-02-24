package com.nass.integration_service.service.impl;

import com.nass.integration_service.constant.ConstantParamCode;
import com.nass.integration_service.dto.ComCfgParameterDto;
import com.nass.integration_service.mapper.ComCfgParameterMapper;
import com.nass.integration_service.repository.ComCfgParameterRepository;
import com.nass.integration_service.service.EfundIntegrationService;
import com.ngvgroup.bpm.core.config.StoredProcedureProperties;
import com.ngvgroup.bpm.core.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.dto.StoredProcedureResponse;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import com.ngvgroup.bpm.core.util.StoredProcedureUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EfundIntegrationServiceImpl implements EfundIntegrationService {

    private final StoredProcedureProperties properties;
    private final ComCfgParameterRepository comCfgParameterRepository;
    private final ComCfgParameterMapper comCfgParameterMapper;
    private final StoredProcedureExecutor storedProcedureExecutor;

    @Override
    public StoredProcedureResponse execute(String procedureName, List<StoredProcedureParameter> parameters) {
        long startTime = System.currentTimeMillis();
        boolean success = false;

        try {
            StoredProcedureUtils.validateProcedureName(procedureName);
            StoredProcedureUtils.validateParameters(parameters);
            if (this.properties.getLogging().isEnabled()) {
                log.info("Executing stored procedure: {}", procedureName);
                if (this.properties.getLogging().isLogParameters()) {
                    log.debug("Parameters: {}", parameters);
                }
            }

            // Lấy thông tin kết nối DB
            ComCfgParameterDto parameterDto = comCfgParameterRepository.findByParamCodeAndIsActiveTrueAndIsDeleteFalse(ConstantParamCode.PARAM_CODE_EFUND_CONNECT_STRING)
                    .map(comCfgParameterMapper::toDto)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "tham số hệ thống với mã " + ConstantParamCode.PARAM_CODE_EFUND_CONNECT_STRING));

            int queryTimeoutSeconds = (int) (this.properties.getTimeout().getQuery() / 1000L);
            StoredProcedureResponse response = storedProcedureExecutor.executeStoredProcedure(procedureName, parameters, parameterDto, queryTimeoutSeconds);
            success = true;
            if (this.properties.getLogging().isEnabled()) {
                long executionTime = System.currentTimeMillis() - startTime;
                String logMessage = StoredProcedureUtils.createLogMessage(procedureName, parameters, executionTime, success);
                log.info(logMessage);
            }
            return response;
        } catch (Exception var10) {
            success = false;
            long executionTime = System.currentTimeMillis() - startTime;
            if (this.properties.getLogging().isEnabled()) {
                String logMessage = StoredProcedureUtils.createLogMessage(procedureName, parameters, executionTime, success);
                log.error(logMessage + ", Error: " + var10.getMessage(), var10);
            }

            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Lỗi khi gọi stored procedure: " + var10.getMessage(), var10);
        }
    }

    @Override
    public StoredProcedureResponse executeWithRetry(String procedureName, Map<String, Object> parameters, int maxRetries) {
        int attempts = 0;
        Exception lastException = null;

        while(attempts < maxRetries) {
            try {
                List<StoredProcedureParameter> paramList = StoredProcedureUtils.convertMapToParameters(parameters);
                return this.execute(procedureName, paramList);
            } catch (Exception e) {
                lastException = e;
                ++attempts;
                if (attempts < maxRetries) {
                    try {
                        Thread.sleep(this.properties.getRetry().getBackoffDelay());
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Thread bị interrupt", ie);
                    }
                }
            }
        }

        throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Thất bại sau " + maxRetries + " lần thử", lastException);
    }

    // Đã tách các hàm quản lý pool/connection và thực thi stored procedure sang class khác

}
