package ngvgroup.com.rpt.features.ctgcfgworkflow.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.CtgCfgWorkflowDto;
import ngvgroup.com.rpt.features.ctgcfgworkflow.dto.ctgcfgworkflow.ExportExcelData;
import ngvgroup.com.rpt.features.ctgcfgworkflow.mapper.CtgCfgWorkflowMapper;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.CtgCfgWorkflow;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgWorkflowRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgWorkflowService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CtgCfgWorkflowServiceImpl implements CtgCfgWorkflowService {

    private final CtgCfgWorkflowRepository repository;
    private final CtgCfgWorkflowMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CtgCfgWorkflowDto> pageWorkflow(String keyword, Pageable pageable) {
        Page<CtgCfgWorkflow> entityPage = repository.pageWorkflowWithVersion(keyword, pageable);
        return entityPage.map(mapper::toDto);
    }

    @Override
    public void createWorkflow(CtgCfgWorkflowDto dto) {
        this.checkData(dto);
        if (repository.existsByWorkflowCodeAndIsDelete(dto.getWorkflowCode(), 0)) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_CODE_CONFLICT);
        }

        CtgCfgWorkflow entity = mapper.toEntity(dto);
        entity.setRecordStatus("APPROVAL");
        repository.save(entity);
    }

    @Override
    public void updateWorkflow(CtgCfgWorkflowDto dto, String workflowCode) {
        this.checkData(dto);
        CtgCfgWorkflow existingEntity = repository.findByWorkflowCodeAndIsDelete(workflowCode, 0);
        if (existingEntity == null) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_NOT_FOUND);
        }
        mapper.updateEntity(dto, existingEntity);
        repository.save(existingEntity);
    }

    @Override
    public void deleteWorkflow(String workflowCode) {
        CtgCfgWorkflow entity = repository.findByWorkflowCodeAndIsDelete(workflowCode, 0);
        if (entity == null) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_NOT_FOUND);
        }

        entity.setIsDelete(1);
        repository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CtgCfgWorkflowDto getDetail(String workflowCode) {
        CtgCfgWorkflow entity = repository.findByWorkflowCodeAndIsDelete(workflowCode, 0);
        if (entity == null) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_NOT_FOUND);
        }

        return mapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName) {
        List<ExportExcelData> data = repository.listWorkflowWithVersion(keyword);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách quy trình");

            // Create header row
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < labels.size(); i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(labels.get(i));

                CellStyle headerStyle = workbook.createCellStyle();
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                cell.setCellStyle(headerStyle);
            }

            // Create data rows
            int rowNum = 1;
            for (ExportExcelData item : data) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(item.getWorkflowCode());
                row.createCell(1).setCellValue(item.getWorkflowName());
                row.createCell(2).setCellValue(item.getInitialStatusCode());
                row.createCell(3).setCellValue(item.getVersionNo().doubleValue());
            }

            // Auto-size columns
            for (int i = 0; i < labels.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);

            ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            throw new BusinessException(StatisticalErrorCode.EXPORT_EXCEL_FAILED);
        }
    }

    private void checkData(CtgCfgWorkflowDto dto) {
        if (dto == null) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_DTO_IS_EMPTY);
        }
        if (dto.getWorkflowCode() == null || dto.getWorkflowCode().trim().isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_CODE_INVALID);
        }
        if (dto.getWorkflowName() == null || dto.getWorkflowName().trim().isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.WORKFLOW_NAME_INVALID);
        }
    }

    public List<CtgCfgWorkflowDto> getList() {
        List<CtgCfgWorkflow> entities = repository.findAll();
        return entities.stream()
                .map(e -> {
                    CtgCfgWorkflowDto dto = new CtgCfgWorkflowDto();
                    dto.setWorkflowCode(e.getWorkflowCode());
                    dto.setWorkflowName(e.getWorkflowName());
                    dto.setVersionNo(e.getVersionNo());
                    return dto;
                })
                .toList();
    }

    @Override
    public Boolean existsByWorkflowCode(String workflowCode) {
        return repository.existsByWorkflowCodeAndIsDelete(workflowCode, 0);
    }

}
