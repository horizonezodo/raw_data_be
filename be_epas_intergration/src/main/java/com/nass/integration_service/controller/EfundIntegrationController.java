package com.nass.integration_service.controller;

import com.nass.integration_service.dto.ProcedureRequest;
import com.nass.integration_service.service.EfundIntegrationService;
import com.ngvgroup.bpm.core.dto.ResponseData;
import com.ngvgroup.bpm.core.dto.StoredProcedureResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/efund")
@RequiredArgsConstructor
public class EfundIntegrationController {
    private final EfundIntegrationService efundIntegrationService;

    @Operation(
        summary = "Thực thi stored procedure",
        description = "API này cho phép thực thi stored procedure với tên và tham số truyền vào.",
        parameters = {
            @Parameter(name = "procedureRequest", description = "Thông tin tên procedure và danh sách tham số", required = true)
        }
    )
    @PostMapping("/exec-procedure")
    public ResponseEntity<ResponseData<StoredProcedureResponse>> execStoreProcedure(@RequestBody ProcedureRequest procedureRequest) {
        StoredProcedureResponse response =  efundIntegrationService.execute(procedureRequest.getProcedureName(), procedureRequest.getParameters());
        return ResponseData.okEntity(response);
    }
}
