package ngvgroup.com.hrm.feature.employee.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import ngvgroup.com.hrm.core.constant.HrmErrorCode;
import ngvgroup.com.hrm.feature.employee.dto.ExportHrmInfEmployeeDTO;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeListDTO;
import ngvgroup.com.hrm.feature.employee.dto.HrmInfEmployeeSearchRequest;
import ngvgroup.com.hrm.feature.employee.repository.HrmInfEmployeeRepository;
import ngvgroup.com.hrm.feature.employee.service.HrmInfEmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HrmInfEmployeeServiceImpl implements HrmInfEmployeeService {
    private final HrmInfEmployeeRepository repository;
    private final ExportExcel exportExcel;

    public HrmInfEmployeeServiceImpl(
            HrmInfEmployeeRepository repository,
            ExportExcel exportExcel) {
        this.repository = repository;
        this.exportExcel = exportExcel;
    }

    @Override
    public Page<HrmInfEmployeeListDTO> search(
            HrmInfEmployeeSearchRequest request,
            Pageable pageable) {
        HrmInfEmployeeSearchRequest normalized = normalizeRequest(request);
        return repository.search(normalized, pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportExcel(
            String fileName,
            HrmInfEmployeeSearchRequest request) throws BusinessException {
        HrmInfEmployeeSearchRequest normalized =
                new HrmInfEmployeeSearchRequest();
        List<ExportHrmInfEmployeeDTO> items = repository.search(
                normalized,
                Pageable.unpaged()
        ).stream()
                .map(this::toExportDto)
                .toList();
        try {
            return exportExcel.exportExcel(items, fileName);
        } catch (Exception ex) {
            throw new BusinessException(HrmErrorCode.EXPORT_EXCEL_ERROR);
        }
    }

    private ExportHrmInfEmployeeDTO toExportDto(HrmInfEmployeeListDTO dto) {
        return new ExportHrmInfEmployeeDTO(
                dto.getEmployeeCode(),
                dto.getEmployeeName(),
                dto.getMobileNumber(),
                dto.getIdentificationId(),
                dto.getPositionName(),
                dto.getOrgUnitName(),
                dto.getStatusName()
        );
    }

    private HrmInfEmployeeSearchRequest normalizeRequest(
            HrmInfEmployeeSearchRequest request) {
        if (request == null) {
            return new HrmInfEmployeeSearchRequest();
        }
        request.setOrgUnitCodes(normalizeList(request.getOrgUnitCodes()));
        request.setEmployeeTypeCodes(
                normalizeList(request.getEmployeeTypeCodes())
        );
        request.setStatusCodes(normalizeList(request.getStatusCodes()));
        request.setKeyword(trimToNull(request.getKeyword()));
        request.setOrgCode(trimToNull(request.getOrgCode()));
        return request;
    }

    private List<String> normalizeList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return List.of();
        }
        return values;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        return trimmed;
    }
}
