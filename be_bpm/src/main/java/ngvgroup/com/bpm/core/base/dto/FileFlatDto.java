package ngvgroup.com.bpm.core.base.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// @AllArgsConstructor  <-- XÓA DÒNG NÀY ĐI
public class FileFlatDto {

    private String processFileCode;
    private String processFileName;
    private String fileId;
    
    // Thứ tự khai báo biến ở đây không quan trọng nếu dùng Constructor thủ công
    private String filePath; 
    private String fileName;
    
    private Long fileSize;

    // --- CONSTRUCTOR KHỚP VỚI THỨ TỰ TRONG QUERY ---
    // Query: processFileCode, processFileName, fileId, fileName, filePath, fileSize
    public FileFlatDto(String processFileCode,
                       String processFileName,
                       Object fileIdObj,   // Hứng bằng Object để chấp nhận UUID hoặc null
                       String fileName,    // <--- Query trả về fileName ở vị trí số 4
                       String filePath,    // <--- Query trả về filePath ở vị trí số 5
                       Number fileSizeObj) { // Hứng bằng Number

        this.processFileCode = processFileCode;
        this.processFileName = processFileName;

        // Xử lý fileId (UUID -> String)
        if (fileIdObj != null) {
            this.fileId = fileIdObj.toString();
        } else {
            this.fileId = null;
        }

        // Gán đúng biến (Dù query trả về thứ tự nào, ta gán vào đúng field đó)
        this.fileName = fileName;
        this.filePath = filePath;

        // Xử lý fileSize (Integer -> Long)
        if (fileSizeObj != null) {
            this.fileSize = fileSizeObj.longValue();
        } else {
            this.fileSize = null;
        }
    }
}