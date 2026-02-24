package ngvgroup.com.bpmn.service.impl;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpmn.dto.LabelExcelDto;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExcelService {
    public ResponseEntity<ByteArrayResource> exportToExcel(List<?> data, List<String> headers, Class<?> modelClass,
            String fileName) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(fileName);
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerStyle);
        }

        int rowCount = 1;
        for (Object obj : data) {
            Row row = sheet.createRow(rowCount++);
            int colIndex = 0;
            for (Field field : modelClass.getDeclaredFields()) {
                if ("id".equals(field.getName())) {
                    continue;
                }
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    log.error("Error: Lỗi ghi dữ liệu ra file Excel {}", e.getMessage());
                }
            }
        }

        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "force-download"));
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx");
            workbook.write(stream);
            workbook.close();
            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                    header, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public byte[] exportToExcelContent(List<?> data, List<LabelExcelDto> labels, Class<?> modelClass) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export");

        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        headerStyle.setFont(font);

        // Write headers with "STT" as first column
        List<String> headerLabels = new ArrayList<>();
        headerLabels.add("STT");
        for (LabelExcelDto label : labels) {
            headerLabels.add(label.getValue());
        }
        for (int i = 0; i < headerLabels.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headerLabels.get(i));
            cell.setCellStyle(headerStyle);
        }

        // Map field names to Field objects in modelClass
        Map<String, Field> fieldMap = new HashMap<>();
        for (Field field : modelClass.getDeclaredFields()) {
            field.setAccessible(true);
            fieldMap.put(field.getName().toLowerCase(), field);
        }

        int rowCount = 1;
        for (Object obj : data) {
            Row row = sheet.createRow(rowCount++);
            int colIndex = 0;
            row.createCell(colIndex++).setCellValue(rowCount - 1);

            for (LabelExcelDto label : labels) {
                String fieldName = label.getKey();
                Field field = fieldMap.get(fieldName.toLowerCase());
                if (field != null) {
                    try {
                        Object value = field.get(obj);
                        row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                    } catch (IllegalAccessException e) {
                        log.error("Error: Lỗi export file Excel");
                        row.createCell(colIndex++).setCellValue("");
                    }
                } else {
                    row.createCell(colIndex++).setCellValue("");
                }
            }
        }

        for (int i = 0; i < headerLabels.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            workbook.write(stream);
            workbook.close();
            return stream.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
