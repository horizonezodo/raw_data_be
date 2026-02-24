package com.nass.integration_service.service;


import com.ngvgroup.bpm.core.dto.StoredProcedureParameter;
import com.ngvgroup.bpm.core.dto.StoredProcedureResponse;

import java.util.List;
import java.util.Map;

public interface EfundIntegrationService {

    StoredProcedureResponse execute(String procedureName, List<StoredProcedureParameter> parameters);
    StoredProcedureResponse executeWithRetry(String procedureName, Map<String, Object> parameters, int maxRetries);
}
