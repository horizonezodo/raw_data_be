package ngvgroup.com.bpm.client.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.bpm.client.service.DocumentTemplateService;
import ngvgroup.com.bpm.client.service.FileService;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.expression.MapAccessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(name = "org.apache.poi.xwpf.usermodel.XWPFDocument")
@ConditionalOnProperty(name = "bpm.client.template.poi.enabled", havingValue = "true", matchIfMissing = true)
public class PoiDocumentTemplateServiceImpl implements DocumentTemplateService {

    private final FileService fileService;
    private final ObjectMapper objectMapper;
    private final ResourceLoader resourceLoader;

    private static final ExpressionParser parser = new SpelExpressionParser();
    // Định dạng ngày tháng mong muốn: dd-MM-yyyy (ví dụ: 01-02-2026)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public byte[] generateFile(String templatePath, String mappingPath, Object dataObject) {
        try (InputStream templateStream = getInputStream(templatePath);
                InputStream mappingStream = getInputStream(mappingPath)) {

            // Load mapping rules from JSON file
            Map<String, String> mappingRules = objectMapper.readValue(mappingStream,
                    new TypeReference<Map<String, String>>() {
                    });

            // 1. Map data (tính toán giá trị từ SpEL)
            Map<String, String> replacements = mapData(dataObject, mappingRules);

            // 2. Fill template (điền dữ liệu vào file Word)
            return fillTemplate(templateStream, replacements);

        } catch (Exception e) {
            log.error("Error generating file: {}", e.getMessage());
            throw new RuntimeException("Lỗi tạo file từ template: " + e.getMessage());
        }
    }

    private byte[] fillTemplate(InputStream templateStream, Map<String, String> data) {
        try (XWPFDocument document = new XWPFDocument(templateStream);
                ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // 1. Thay thế text trong các đoạn văn (Paragraphs)
            for (XWPFParagraph p : document.getParagraphs()) {
                replaceTextInParagraph(p, data);
            }

            // 2. Thay thế text trong các bảng (Tables)
            for (XWPFTable table : document.getTables()) {
                for (XWPFTableRow row : table.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            replaceTextInParagraph(p, data);
                        }
                    }
                }
            }

            document.write(out);
            return out.toByteArray();

        } catch (Exception e) {
            log.error("Generate DOCX error: {}", e.getMessage());
            throw new RuntimeException("Lỗi xuất biểu mẫu: " + e.getMessage());
        }
    }

    private InputStream getInputStream(String path) {
        try {
            if (path.startsWith("classpath:")) {
                Resource resource = resourceLoader.getResource(path);
                if (!resource.exists()) {
                    throw new RuntimeException("File not found in classpath: " + path);
                }
                return resource.getInputStream();
            } else {
                return fileService.downloadFileStream(path);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading file from path: " + path, e);
        }
    }

    private Map<String, String> mapData(Object rootContext, Map<String, String> mappingRules) {
        Map<String, String> result = new HashMap<>();
        StandardEvaluationContext context = new StandardEvaluationContext(rootContext);

        // [QUAN TRỌNG] Thêm MapAccessor để SpEL đọc được dữ liệu trong Map (fix lỗi dữ
        // liệu bị trắng)
        context.addPropertyAccessor(new MapAccessor());

        for (Map.Entry<String, String> entry : mappingRules.entrySet()) {
            String placeholder = entry.getKey();
            String expression = entry.getValue();

            try {
                // Đánh giá biểu thức SpEL
                Object value = parser.parseExpression(expression).getValue(context);
                // Format giá trị (Date, String...) thành chuỗi hiển thị
                result.put(placeholder, formatValue(value));
            } catch (Exception e) {
                // Nếu lỗi expression, để trống thay vì gây lỗi chương trình
                log.warn("Could not evaluate expression '{}' for key '{}': {}", expression, placeholder,
                        e.getMessage());
                result.put(placeholder, "");
            }
        }
        return result;
    }

    private String formatValue(Object value) {
        if (value == null) {
            return "";
        }

        // 1. Xử lý Date (java.util.Date)
        if (value instanceof Date) {
            return DATE_FORMATTER.format(((Date) value).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
        }

        // 2. Xử lý LocalDate
        if (value instanceof LocalDate) {
            return DATE_FORMATTER.format((LocalDate) value);
        }

        // 3. [QUAN TRỌNG] Xử lý String ngày tháng
        if (value instanceof String) {
            String strVal = (String) value;

            // Kiểm tra sơ bộ: chuỗi quá ngắn hoặc không bắt đầu bằng số thì trả về luôn
            if (strVal.length() < 10 || !Character.isDigit(strVal.charAt(0))) {
                return strVal;
            }

            try {
                // Thử parse format ISO đầy đủ (2026-02-01T00:00:00.000+07:00)
                ZonedDateTime zdt = ZonedDateTime.parse(strVal);
                return DATE_FORMATTER.format(zdt);
            } catch (Exception e1) {
                try {
                    // Thử parse format ngắn (2026-02-01)
                    LocalDate ld = LocalDate.parse(strVal);
                    return DATE_FORMATTER.format(ld);
                } catch (Exception e2) {
                    // Không phải ngày tháng hợp lệ -> Trả về nguyên gốc
                    return strVal;
                }
            }
        }

        return value.toString();
    }

    private void replaceTextInParagraph(XWPFParagraph paragraph, Map<String, String> replacements) {
        String text = paragraph.getText();
        if (text == null || text.isEmpty())
            return;

        boolean hasReplacement = false;
        // Kiểm tra xem đoạn văn có chứa placeholder nào không
        for (String key : replacements.keySet()) {
            if (text.contains(key)) {
                String val = replacements.get(key);
                text = text.replace(key, val == null ? "" : val);
                hasReplacement = true;
            }
        }

        // Nếu có thay thế, cập nhật lại Run trong POI để tránh lỗi vỡ format
        if (hasReplacement) {
            // Xóa tất cả các Run hiện tại trong Paragraph
            while (paragraph.getRuns().size() > 0) {
                paragraph.removeRun(0);
            }
            // Tạo một Run mới chứa toàn bộ text đã thay thế
            XWPFRun newRun = paragraph.createRun();
            newRun.setText(text);

            // Lưu ý: Việc xóa và tạo lại run có thể làm mất một số định dạng cục bộ
            // (bold/italic)
            // nếu định dạng đó chỉ áp dụng cho một phần của đoạn văn.
            // Tuy nhiên với template biểu mẫu hành chính thông thường thì cách này an toàn
            // và hiệu quả nhất.
        }
    }
}