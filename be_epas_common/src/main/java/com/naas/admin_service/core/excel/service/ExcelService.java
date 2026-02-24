package com.naas.admin_service.core.excel.service;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.excel.dto.LabelExcelDto;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ExcelService {
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
            fieldMap.put(field.getName().toLowerCase(), field);
        }

        int rowCount = 1;
        for (Object obj : data) {
            Row row = sheet.createRow(rowCount++);
            int colIndex = 0;
            row.createCell(colIndex++).setCellValue((double) rowCount - 1);

            for (LabelExcelDto label : labels) {
                String fieldName = label.getKey();
                try {
                    Object value = fieldMap.get(fieldName.toLowerCase()).get(obj);
                    row.createCell(colIndex++).setCellValue(value.toString());
                } catch (IllegalAccessException e) {
                    log.error("Error: Lỗi export file Excel");
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
            return new byte[0];
        }
    }

    public ResponseEntity<ByteArrayResource> exportToExcel(List<?> data, List<String> headers, Class<?> modelClass, String fileName) {

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
                try {
                    Object value = field.get(obj);
                    row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    log.error("Error: Lỗi ghi dữ liệu ra file Excel {}", e.getMessage());
                    throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR, "Lỗi khi ghi dữ liệu ra file Excel: " + e.getMessage());
                }
            }
        }

        for (int i = 0; i < headers.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            String encodedFileName = URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8).replace("+", "%20");

            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);
            workbook.write(stream);
            workbook.close();
            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()),
                    header, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
