package ngvgroup.com.rpt.features.ctgcfgtransition.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionCond;
import ngvgroup.com.rpt.features.ctgcfgtransition.repository.CtgCfgTransitionCondRepository;
import ngvgroup.com.rpt.features.ctgcfgtransition.service.CtgCfgTransitionCondService;
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
@Transactional
public class CtgCfgTransitionCondServiceImpl implements CtgCfgTransitionCondService {

    private final CtgCfgTransitionCondRepository conditionRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ByteArrayResource> exportToExcel(
            List<String> labels,
            String keyword,
            String fileName,
            String transitionCode) {

        List<CtgCfgTransitionCond> conditions = conditionRepository
                .findByTransitionCodeAndIsDelete(transitionCode, 0);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách điều kiện");

            createHeaderRow(workbook, sheet, labels);
            createDataRows(sheet, conditions, keyword);

            ByteArrayResource resource = new ByteArrayResource(writeWorkbookToBytes(workbook));

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
            sheet.autoSizeColumn(i);
        }
    }

    private void createDataRows(
            Sheet sheet,
            List<CtgCfgTransitionCond> conditions,
            String keyword) {

        int rowNum = 1;

        for (CtgCfgTransitionCond cond : conditions) {
            if (!matchesKeyword(cond, keyword)) continue;

            Row row = sheet.createRow(rowNum++);
            fillConditionRow(row, cond);
        }
    }

    private boolean matchesKeyword(CtgCfgTransitionCond condition, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) return true;
        return containsKeyword(condition, keyword);
    }

    private void fillConditionRow(Row row, CtgCfgTransitionCond condition) {
        row.createCell(0).setCellValue(defaultString(condition.getConditionType()));
        row.createCell(1).setCellValue(defaultString(condition.getExpressionSql()));
        row.createCell(2).setCellValue(condition.getConditionNo() != null ? condition.getConditionNo() : 0);
        row.createCell(3).setCellValue(
                condition.getIsMandatory() != null && condition.getIsMandatory() == 1 ? "Có" : "Không"
        );
    }

    private String defaultString(String val) {
        return val != null ? val : "";
    }

    private byte[] writeWorkbookToBytes(Workbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return outputStream.toByteArray();
    }

    private boolean containsKeyword(CtgCfgTransitionCond condition, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return (condition.getConditionType() != null &&
                condition.getConditionType().toLowerCase().contains(lowerKeyword)) ||
                (condition.getExpressionSql() != null &&
                        condition.getExpressionSql().toLowerCase().contains(lowerKeyword))
                ||
                (condition.getConditionType() != null &&
                        condition.getConditionType().toLowerCase().contains(lowerKeyword));
    }
}