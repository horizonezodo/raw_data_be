package ngvgroup.com.rpt.features.ctgcfgstatus.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.CtgCfgStatusDto;
import ngvgroup.com.rpt.features.ctgcfgstatus.dto.ExportExcelData;
import ngvgroup.com.rpt.features.ctgcfgstatus.mapper.CtgCfgStatusMapper;
import ngvgroup.com.rpt.features.ctgcfgstatus.model.CtgCfgStatus;
import ngvgroup.com.rpt.features.ctgcfgstatus.repository.CtgCfgStatusRepository;
import ngvgroup.com.rpt.features.ctgcfgstatus.service.CtgCfgStatusService;
import ngvgroup.com.rpt.features.ctgcfgstatus.service.CtgCfgStatusSlaService;
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
public class CtgCfgStatusServiceImpl implements CtgCfgStatusService {

    private final CtgCfgStatusRepository repository;
    private final CtgCfgStatusMapper mapper;
    private final CtgCfgStatusSlaService service;

    @Override
    @Transactional(readOnly = true)
    public Page<CtgCfgStatusDto> pageStatus(String keyword, Pageable pageable) {
        return repository.pageStatusWithType(keyword, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CtgCfgStatusDto> getAllStatusByType(String type) {
        List<CtgCfgStatus> entities;

        if (type == null || type.isBlank()) {
            entities = repository.findByIsDeleteOrderBySortNumberAsc(0);
        } else {
            entities = repository.findByAndStatusTypeCodeAndIsDeleteOrderBySortNumberAsc(type, 0);
        }

        return entities.stream().map(mapper::toDto).toList();
    }

    @Override
    public void createStatus(CtgCfgStatusDto dto) {
        this.checkData(dto);
        if (repository.existsByStatusCodeAndIsDelete(dto.getStatusCode(), 0)) {
            throw new BusinessException(StatisticalErrorCode.STATUS_CODE_CONFLICT);
        }

        CtgCfgStatus entity = mapper.toEntity(dto);
        entity.setRecordStatus("APPROVAL");
        entity.setIsActive(1);
        repository.save(entity);
        dto.getSlaDto().setStatusCode(dto.getStatusCode());
        this.service.create(dto.getSlaDto());
    }

    @Override
    public void updateStatus(CtgCfgStatusDto dto, String statusCode) {
        this.checkData(dto);
        CtgCfgStatus existingEntity = repository.findByStatusCodeAndIsDelete(statusCode, 0);
        if (existingEntity == null) {
            throw new BusinessException(StatisticalErrorCode.STATUS_NOT_FOUND);
        }

        mapper.updateEntity(dto, existingEntity);
        repository.save(existingEntity);
        Long id = this.service.getIdByStatusCode(dto.getStatusCode());
        service.update(id, dto.getSlaDto());
    }

    @Override
    public void deleteStatus(String statusCode) {
        CtgCfgStatus entity = repository.findByStatusCodeAndIsDelete(statusCode, 0);
        if (entity == null) {
            throw new BusinessException(StatisticalErrorCode.STATUS_NOT_FOUND);
        }

        entity.setIsDelete(1);
        repository.save(entity);
        Long id = this.service.getIdByStatusCode(statusCode);
        service.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CtgCfgStatusDto getDetail(String statusCode) {
        CtgCfgStatus entity = repository.findByStatusCodeAndIsDelete(statusCode, 0);
        if (entity == null) {
            throw new BusinessException(StatisticalErrorCode.STATUS_NOT_FOUND);
        }
        CtgCfgStatusDto dto = mapper.toDto(entity);
        Long id = this.service.getIdByStatusCode(statusCode);
        dto.setSlaDto(this.service.findById(id));
        return dto;
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName) {
        List<ExportExcelData> data = repository.listStatusWithType(keyword);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách trạng thái");

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
                row.createCell(0).setCellValue(item.getStatusCode());
                row.createCell(1).setCellValue(item.getStatusName());
                row.createCell(2).setCellValue(item.getStatusTypeCode() + " - " + item.getStatusTypeName());
                row.createCell(3).setCellValue(item.getDescription());
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

    @Override
    public List<String> getAllCode() {
        return this.repository.getAllStatusCode();
    }

    private void checkData(CtgCfgStatusDto dto) {
        if (dto == null) {
            throw new BusinessException(StatisticalErrorCode.STATUS_DTO_IS_EMPTY);
        }
        if (dto.getStatusCode() == null || dto.getStatusCode().trim().isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.STATUS_CODE_INVALID);
        }
        if (dto.getStatusName() == null || dto.getStatusName().trim().isEmpty()) {
            throw new BusinessException(StatisticalErrorCode.STATUS_NAME_INVALID);
        }
        if (dto.getSortNumber() == null || dto.getSortNumber() <= 0) {
            throw new BusinessException(StatisticalErrorCode.SORT_NUMBER_INVALID);
        }
    }
}
