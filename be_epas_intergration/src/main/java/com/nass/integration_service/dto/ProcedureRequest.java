package com.nass.integration_service.dto;

import com.ngvgroup.bpm.core.dto.StoredProcedureParameter;
import lombok.Data;

import java.util.List;

@Data
public class ProcedureRequest {
    private String procedureName;
    private List<StoredProcedureParameter> parameters;
}
