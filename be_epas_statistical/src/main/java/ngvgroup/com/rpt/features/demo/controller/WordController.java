package ngvgroup.com.rpt.features.demo.controller;

import com.ngvgroup.bpm.core.logging.activity.annotation.LogActivity;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.demo.service.ViewWordService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Map;

@RestController
@RequestMapping("/view-word")
@CrossOrigin("*")
@RequiredArgsConstructor
public class WordController {
    private final ViewWordService viewExcelService;

    @LogActivity(function = "Lấy nội dung Word dạng HTML")
    @GetMapping("/html")
    public ResponseEntity<Map<String, String>> getWordSectionsAsHtml() {
        try {
            // Load file từ classpath
            ClassPathResource resource = new ClassPathResource("templates/doc-template.docx");

            try (InputStream inputStream = resource.getInputStream()) {
                Map<String, String> result = viewExcelService.getWordSectionsAsHtml(inputStream);
                return ResponseEntity.ok(result);
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}