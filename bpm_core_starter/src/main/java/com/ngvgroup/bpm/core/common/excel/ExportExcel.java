package com.ngvgroup.bpm.core.common.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

@Component
public class ExportExcel {
    public ResponseEntity<byte[]> exportExcel(List<?> data, String fileName) throws Exception {

        if (data == null || data.isEmpty()) {
            throw new RuntimeException("Data is empty!");
        }

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DATA");
        Class<?> clazz = data.get(0).getClass();

        Field[] fields = clazz.getDeclaredFields();
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        // Tạo header từ annotation
        int colIndex = 0;
        for (Field field : fields) {
            ExcelColumn col = field.getAnnotation(ExcelColumn.class);
            if (col != null) {
                Cell cell = headerRow.createCell(colIndex++);
                cell.setCellValue(col.value());
                cell.setCellStyle(headerStyle);
            }
        }

        // Fill data
        int rowIndex = 1;
        for (Object obj : data) {
            Row row = sheet.createRow(rowIndex++);
            colIndex = 0;

            for (Field field : fields) {
                ExcelColumn col = field.getAnnotation(ExcelColumn.class);
                if (col != null) {
                    field.setAccessible(true);
                    Object value = field.get(obj);
                    Cell cell = row.createCell(colIndex++);

                    if (value instanceof Number) {
                        cell.setCellValue(new BigDecimal(value.toString()).doubleValue());
                    } else if (value instanceof Date) {
                        cell.setCellValue(value.toString());
                    } else {
                        cell.setCellValue(value == null ? "" : value.toString());
                    }
                }
            }
        }

        // Auto-size columns
        for (int i = 0; i < colIndex; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(out.toByteArray());
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
