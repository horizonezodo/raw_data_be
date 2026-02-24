package ngvgroup.com.rpt.features.ctgcfgworkflow.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgworkflow.model.WorkflowTransition;
import ngvgroup.com.rpt.features.ctgcfgworkflow.repository.CtgCfgWorkflowTransitionRepository;
import ngvgroup.com.rpt.features.ctgcfgworkflow.service.CtgCfgWorkflowTransitionService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
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
@Slf4j
@Transactional
public class CtgCfgWorkflowTransitionServiceImpl implements CtgCfgWorkflowTransitionService {

    private final CtgCfgWorkflowTransitionRepository transitionRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ByteArrayResource> exportToExcel(
            List<String> labels, String keyword, String fileName, String workflowCode) {

        List<WorkflowTransition> transitions =
                transitionRepository.findByWorkflowCodeAndIsDelete(workflowCode, 0);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hành động");

            createHeaderRow(workbook, sheet, labels);
            writeDataRows(sheet, transitions, keyword);
            autoSizeColumns(sheet, labels.size());

            ByteArrayResource resource = buildExcelResource(workbook);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (IOException e) {
            throw new BusinessException(StatisticalErrorCode.EXPORT_EXCEL_FAILED);
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet, List<String> labels) {
        Row headerRow = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        for (int i = 0; i < labels.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(labels.get(i));
            cell.setCellStyle(headerStyle);
        }
    }

    private void writeDataRows(
            Sheet sheet,
            List<WorkflowTransition> transitions,
            String keyword) {

        int rowNum = 1;

        for (WorkflowTransition transition : transitions) {
            if (!matchKeyword(transition, keyword)) {
                continue;
            }
            createDataRow(sheet, rowNum++, transition);
        }
    }

    private boolean matchKeyword(WorkflowTransition transition, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        // Giữ nguyên logic sử dụng containsKeyword như ban đầu
        return containsKeyword(transition, keyword);
    }

    private void createDataRow(Sheet sheet, int rowNum, WorkflowTransition transition) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(
                transition.getTransitionCode() != null ? transition.getTransitionCode() : "");

        row.createCell(1).setCellValue(
                transition.getTransitionName() != null ? transition.getTransitionName() : "");

        row.createCell(2).setCellValue(
                transition.getFromStatusName() != null ? transition.getFromStatusName() : "");

        row.createCell(3).setCellValue(
                transition.getToStatusName() != null ? transition.getToStatusName() : "");

        row.createCell(4).setCellValue(
                transition.getSortNumber() != null ? transition.getSortNumber() : 0);
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private ByteArrayResource buildExcelResource(Workbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return new ByteArrayResource(outputStream.toByteArray());
    }


    private boolean containsKeyword(WorkflowTransition transition, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return (transition.getTransitionCode() != null &&
                transition.getTransitionCode().toLowerCase().contains(lowerKeyword)) ||
                (transition.getTransitionName() != null &&
                        transition.getTransitionName().toLowerCase().contains(lowerKeyword))
                ||
                (transition.getFromStatusName() != null &&
                        transition.getFromStatusName().toLowerCase().contains(lowerKeyword))
                ||
                (transition.getToStatusName() != null &&
                        transition.getToStatusName().toLowerCase().contains(lowerKeyword));
    }
}
