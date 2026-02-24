package com.naas.admin_service.features.category.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TemplateResDto {
    @ExcelColumn("Mã file template")
    private String templateCode;
    @ExcelColumn("Tên file template")
    private String fileName;
    @ExcelColumn("Mô tả")
    private String description;

    private byte[] fileData;
    private String filePath;
    private BigDecimal fileSize;
    @ExcelColumn("File mapping")
    private String fileMappingPath;

    public TemplateResDto(String templateCode, String fileName, String description, String filePath, BigDecimal fileSize) {
        this.templateCode = templateCode;
        this.fileName = fileName;
        this.description = description;
        this.filePath = filePath;
        this.fileSize = fileSize;
    }

    public TemplateResDto(String templateCode, String fileName, String description, String filePath, BigDecimal fileSize, String fileMappingPath) {
        this.templateCode = templateCode;
        this.fileName = fileName;
        this.description = description;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.fileMappingPath = fileMappingPath;
    }
}
