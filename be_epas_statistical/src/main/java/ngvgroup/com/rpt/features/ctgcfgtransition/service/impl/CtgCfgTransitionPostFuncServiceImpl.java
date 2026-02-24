package ngvgroup.com.rpt.features.ctgcfgtransition.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.features.ctgcfgtransition.model.CtgCfgTransitionPostFunc;
import ngvgroup.com.rpt.features.ctgcfgtransition.repository.CtgCfgTransitionPostFuncRepository;
import ngvgroup.com.rpt.features.ctgcfgtransition.service.CtgCfgTransitionPostFuncService;
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
public class CtgCfgTransitionPostFuncServiceImpl implements CtgCfgTransitionPostFuncService {

    private final CtgCfgTransitionPostFuncRepository postFuncRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ByteArrayResource> exportToExcel(
            List<String> labels, String keyword, String fileName, String transitionCode) {

        List<CtgCfgTransitionPostFunc> postFunctions = postFuncRepository
                .findByTransitionCodeAndIsDelete(transitionCode, 0);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Danh sách hậu xử lý");

            createHeaderRow(workbook, sheet, labels);
            writeDataRows(sheet, postFunctions, keyword);

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
            sheet.autoSizeColumn(i);
        }
    }

    private void writeDataRows(Sheet sheet, List<CtgCfgTransitionPostFunc> postFunctions, String keyword) {
        int rowNum = 1;

        for (CtgCfgTransitionPostFunc postFunc : postFunctions) {
            if (!matchKeyword(postFunc, keyword)) {
                continue;
            }
            createDataRow(sheet, rowNum++, postFunc);
        }
    }

    private boolean matchKeyword(CtgCfgTransitionPostFunc postFunc, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return true;
        }
        return containsKeyword(postFunc, keyword);
    }

    private void createDataRow(Sheet sheet, int rowNum, CtgCfgTransitionPostFunc postFunc) {
        Row row = sheet.createRow(rowNum);

        row.createCell(0).setCellValue(
                postFunc.getPostFunctionType() != null ? postFunc.getPostFunctionType() : "");

        row.createCell(1).setCellValue(
                postFunc.getExpressionSql() != null ? postFunc.getExpressionSql() : "");

        row.createCell(2).setCellValue(
                postFunc.getPostFunctionNo() != null ? postFunc.getPostFunctionNo() : 0);

        row.createCell(3).setCellValue(
                postFunc.getIsAsync() != null && postFunc.getIsAsync() == 0 ? "Có" : "Không");
    }

    private ByteArrayResource buildExcelResource(Workbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        return new ByteArrayResource(outputStream.toByteArray());
    }

    private boolean containsKeyword(CtgCfgTransitionPostFunc postFunc, String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return (postFunc.getPostFunctionType() != null &&
                postFunc.getPostFunctionType().toLowerCase().contains(lowerKeyword)) ||
                (postFunc.getExpressionSql() != null &&
                        postFunc.getExpressionSql().toLowerCase().contains(lowerKeyword))
                ||
                (postFunc.getPostFunctionType() != null &&
                        postFunc.getPostFunctionType().toLowerCase().contains(lowerKeyword));
    }
}