package ngvgroup.com.rpt.features.demo.service;

import org.springframework.stereotype.Service;
import com.aspose.words.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ViewWordService {

    public Map<String, String> getWordSectionsAsHtml(InputStream filePath) throws Exception {
        // 1. Load tài liệu Word
        Document doc = new Document(filePath);

        Map<String, String> sectionHtmlMap = new LinkedHashMap<>();

        // 2. Xuất từng section thành HTML
        int sectionIndex = 1;
        for (Section section : doc.getSections()) {
            // Tạo document tạm chỉ chứa 1 section
            Document tempDoc = new Document();
            tempDoc.removeAllChildren(); // xóa section mặc định

            Section newSection = (Section) tempDoc.importNode(section, true, ImportFormatMode.KEEP_SOURCE_FORMATTING);
            tempDoc.appendChild(newSection);

            // Lưu ra stream dưới dạng HTML
            HtmlSaveOptions options = new HtmlSaveOptions(SaveFormat.HTML);
            options.setExportImagesAsBase64(true);

            ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
            tempDoc.save(htmlStream, options);

            String htmlContent = htmlStream.toString(StandardCharsets.UTF_8);
            sectionHtmlMap.put("Section " + sectionIndex, htmlContent);

            sectionIndex++;
        }

        return sectionHtmlMap;
    }
}

