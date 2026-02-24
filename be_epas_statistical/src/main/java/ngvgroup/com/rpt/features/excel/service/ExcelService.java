package ngvgroup.com.rpt.features.excel.service;

import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.rpt.features.report.dto.LabelExcelDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ngvgroup.com.rpt.core.constant.VariableConstants.ERROR_EXPORT_EXCEL_LOG;

@Slf4j
@Service
public class ExcelService {

    private Object getFieldValue(Object obj, String fieldName) {
        try {
            PropertyDescriptor pd = new PropertyDescriptor(fieldName, obj.getClass());
            Method getter = pd.getReadMethod();
            if (getter == null) return null;
            return getter.invoke(obj);
        } catch (Exception e) {
            log.error("Cannot read field: {}", fieldName);
            return null;
        }
    }

    private Workbook buildWorkbook(List<?> data, List<String> headers, Class<?> modelClass, String fileName) {
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
                if ("id".equals(field.getName())) continue;

                Object value = getFieldValue(obj, field.getName());
                row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
            }
        }

        for (int i = 0; i < headers.size(); i++) sheet.autoSizeColumn(i);

        return workbook;
    }

    private ResponseEntity<ByteArrayResource> buildResponse(Workbook workbook, HttpHeaders headers) {
        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            workbook.write(stream);
            workbook.close();

            return new ResponseEntity<>(new ByteArrayResource(stream.toByteArray()), headers, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(ERROR_EXPORT_EXCEL_LOG, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<ByteArrayResource> exportToExcel(List<?> data, List<String> headers, Class<?> modelClass, String fileName) {

        Workbook workbook = buildWorkbook(data, headers, modelClass, fileName);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName + ".xlsx");

        return  buildResponse(workbook, header);
    }

    public ResponseEntity<ByteArrayResource> exportToExcelV2(List<?> data, List<String> headers, Class<?> modelClass, String fileName) {

        Workbook workbook = buildWorkbook(data, headers, modelClass, fileName);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(new MediaType("application", "force-download"));

        String encodedFileName = URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8);
        header.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName);

        return buildResponse(workbook, header);
    }

    public byte[] exportToExcelContent(List<?> data, List<LabelExcelDto> labels, Class<?> modelClass) {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Export");

        createHeaderRow(workbook, sheet, labels);

        writeDataRows(sheet, data, labels, modelClass);

        autoSizeColumns(sheet, labels.size() + 1);

        try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
            workbook.write(stream);
            workbook.close();
            return stream.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
            return new byte[0];
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet, List<LabelExcelDto> labels) {
        Row headerRow = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);

        List<String> headers = new ArrayList<>();
        headers.add("STT");
        for (LabelExcelDto label : labels) headers.add(label.getValue());

        for (int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(style);
        }
    }

    private void writeDataRows(Sheet sheet, List<?> data, List<LabelExcelDto> labels, Class<?> modelClass) {
        int rowCount = 1;

        for (Object obj : data) {
            Row row = sheet.createRow(rowCount);
            int colIndex = 0;

            row.createCell(colIndex++).setCellValue(rowCount);

            for (LabelExcelDto label : labels) {
                try {
                    modelClass.getDeclaredField(label.getKey());
                } catch (NoSuchFieldException e) {
                    log.warn("Field {} not found in model {}", label.getKey(), modelClass.getSimpleName());
                }
                Object value = getFieldValue(obj, label.getKey());
                row.createCell(colIndex++).setCellValue(value != null ? value.toString() : "");
            }

            rowCount++;
        }
    }

    private void autoSizeColumns(Sheet sheet, int columns) {
        for (int i = 0; i < columns; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}
