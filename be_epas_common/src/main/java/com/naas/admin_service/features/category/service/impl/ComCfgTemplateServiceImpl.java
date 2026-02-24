package com.naas.admin_service.features.category.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.deepoove.poi.XWPFTemplate;
import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.core.utils.JasperUtils;
import com.naas.admin_service.core.utils.Utils;
import com.naas.admin_service.features.category.dto.ReportReqDto;
import com.naas.admin_service.features.category.dto.TemplateReqDto;
import com.naas.admin_service.features.category.dto.TemplateReqExcelDto;
import com.naas.admin_service.features.category.dto.TemplateResDto;
import com.naas.admin_service.features.category.model.ComCfgTemplate;
import com.naas.admin_service.features.category.repository.ComCfgTemplateRepository;
import com.naas.admin_service.features.category.service.ComCfgTemplateService;
import com.naas.admin_service.features.common.service.MinIOService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

@Service
public class ComCfgTemplateServiceImpl extends BaseStoredProcedureService implements ComCfgTemplateService {

    private final ComCfgTemplateRepository comCfgTemplateRepository;
    private final MinIOService minioService;
    private final JasperUtils jasperUtils;
    private final ObjectMapper objectMapper;

    public ComCfgTemplateServiceImpl(ComCfgTemplateRepository comCfgTemplateRepository, MinIOService minioService, JasperUtils jasperUtils, ObjectMapper objectMapper) {
        super();
        this.comCfgTemplateRepository = comCfgTemplateRepository;
        this.minioService = minioService;
        this.jasperUtils = jasperUtils;
        this.objectMapper = objectMapper;
    }

    @Override
    public Page<TemplateResDto> searchListTemplate(String keyword, Pageable pageable) {
        return comCfgTemplateRepository.searchListTemplate(keyword, pageable);
    }

    @Override
    public TemplateResDto createTemplate(TemplateReqDto dto, MultipartFile file, MultipartFile fileMapping) {
        String filePath = file.getOriginalFilename();
        String fileMappingPath = null;
        String prefixPath = Utils.buildTemplatePath(dto.getTemplateCode());

        // Upload file lên MinIO
        minioService.uploadFile(prefixPath, file);


        if (Objects.nonNull(fileMapping) && !fileMapping.isEmpty()) {
            fileMappingPath = fileMapping.getOriginalFilename();
            minioService.uploadFile(prefixPath, fileMapping);
        }

        double fileSizeInMb = (double) file.getSize() / (1024 * 1024);
        BigDecimal roundedSize = BigDecimal.valueOf(fileSizeInMb).setScale(3, RoundingMode.HALF_UP);

        // Lưu entity
        ComCfgTemplate entity = new ComCfgTemplate();
        entity.setTemplateCode(dto.getTemplateCode());
        entity.setFileName(dto.getFileName());
        entity.setFilePath(filePath);
        entity.setFileSize(roundedSize);
        entity.setRecordStatus(Constant.APPROVAL);
        entity.setRequiredAction(Constant.CREATE);
        entity.setDescription(dto.getDescription());
        entity.setMappingFilePath(fileMappingPath);

        comCfgTemplateRepository.save(entity);
        return new TemplateResDto();
    }

    @Override
    public TemplateResDto updateTemplate(String templateCode, TemplateReqDto dto, MultipartFile file, MultipartFile fileMapping) {
        String prefixPath = Utils.buildTemplatePath(templateCode);
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);

        // Cập nhật các trường nếu dto không null
        if (dto.getFileName() != null) {
            entity.setFileName(dto.getFileName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        // Nếu có file mới, xóa file cũ rồi upload và cập nhật thông tin file
        if (file != null && !file.isEmpty() && dto.getFileChanged()) {
            removeFile(templateCode);
            String fileName = updateFile(entity, file, prefixPath, false);
            entity.setFilePath(fileName);
        }

        if (dto.getFileMappingChanged()) {
            removeFileMapping(templateCode);
            if (fileMapping != null && !fileMapping.isEmpty()) {
                String fileName = updateFile(entity, fileMapping, prefixPath, true);
                entity.setMappingFilePath(fileName);
            } else {
                entity.setMappingFilePath(null);
            }
        }

        comCfgTemplateRepository.save(entity);

        return new TemplateResDto(
                entity.getTemplateCode(),
                entity.getFileName(),
                entity.getDescription(),
                null,
                entity.getFilePath(),
                entity.getFileSize(),
                entity.getMappingFilePath());
    }

    @Override
    public TemplateResDto getDetail(String templateCode) {
        ComCfgTemplate entity = comCfgTemplateRepository.findEntityByTemplateCode(templateCode).orElse(null);
        if (entity == null) {
            return null;
        }
        return new TemplateResDto(
                entity.getTemplateCode(),
                entity.getFileName(),
                entity.getDescription(),
                null,
                entity.getFilePath(),
                entity.getFileSize(),
                entity.getMappingFilePath());
    }

    @Override
    public TemplateResDto getDetailWithFile(String templateCode) {
        ComCfgTemplate entity = comCfgTemplateRepository.findEntityByTemplateCode(templateCode).orElse(null);
        if (entity == null) {
            return null;
        }
        byte[] fileData = downloadFileTemplate(templateCode);
        return new TemplateResDto(
                entity.getTemplateCode(),
                entity.getFileName(),
                entity.getDescription(),
                fileData,
                entity.getFilePath(),
                entity.getFileSize(),
                entity.getMappingFilePath());
    }

    @Override
    public byte[] downloadFileTemplate(String templateCode) {
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);
        return downloadTemplateFile(entity, entity.getFilePath());
    }

    @Override
    public byte[] downloadFileMappingTemplate(String templateCode) {
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);
        return downloadTemplateFile(entity, entity.getMappingFilePath());
    }

    @Override
    public void deleteTemplate(String templateCode) {
        removeFile(templateCode);
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);
        entity.setIsActive(0);
        entity.setIsDelete(1);
        comCfgTemplateRepository.save(entity);
    }

    @Override
    public void removeFile(String templateCode) {
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);
        String filePath = Utils.buildTemplatePath(entity.getTemplateCode()) + entity.getFilePath();
        minioService.deleteFile(filePath);
        entity.setFilePath("");
        entity.setFileSize(BigDecimal.ZERO);
        comCfgTemplateRepository.save(entity);
    }

    @Override
    public void removeFileMapping(String templateCode) {
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);
        if (Objects.isNull(entity.getMappingFilePath())) {
            return;
        }
        String fileMappingPath = Utils.buildTemplatePath(entity.getTemplateCode()) + entity.getMappingFilePath();
        minioService.deleteFile(fileMappingPath);
        entity.setMappingFilePath("");
        comCfgTemplateRepository.save(entity);
    }

    @Override
    public boolean existsByTemplateCode(String templateCode) {
        return comCfgTemplateRepository.findEntityByTemplateCode(templateCode).isPresent();
    }

    @Override
    public List<TemplateResDto> generateExcelFile(TemplateReqExcelDto req) {
        return comCfgTemplateRepository.searchToExportExcel(
                req.getTemplateCode(), req.getFileName(), req.getDescription());
    }

    @Override
    public byte[] generateReport(ReportReqDto request) {
        if (request == null) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "request");
        }
        String templateCode = request.getTemplateCode();
        String format = request.getFormat();
        validateTemplateCode(templateCode);
        ComCfgTemplate entity = getComCfgTemplateEntity(templateCode);
        String filePath = entity.getFilePath();
        if (filePath == null || filePath.isBlank()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "filePath");
        }
        byte[] templateData = downloadTemplateFile(entity, filePath);
        byte[] jsonData = resolveJsonData(request, entity);
        if (isDocxTemplate(filePath)) {
            return generateDocxReport(templateData, jsonData, format);
        }
        if (!isJrxmlTemplate(filePath)) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "filePath");
        }
        try (InputStream jrxmlStream = new ByteArrayInputStream(templateData);
                InputStream jsonStream = new ByteArrayInputStream(jsonData)) {
            String exportFormat = normalizeFormat(format);
            var jasperPrint = jasperUtils.fillFromJson(jrxmlStream, jsonStream);
            return switch (exportFormat) {
                case Constant.FORMAT_WORD -> jasperUtils.exportToWord(jasperPrint);
                case Constant.FORMAT_XLSX -> jasperUtils.exportToExcel(jasperPrint);
                default -> JasperExportManager.exportReportToPdf(jasperPrint);
            };
        } catch (JRException | IOException ex) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isJrxmlTemplate(String filePath) {
        return filePath.toLowerCase(Locale.ROOT).endsWith(".jrxml");
    }

    private boolean isDocxTemplate(String filePath) {
        return filePath.toLowerCase(Locale.ROOT).endsWith(".docx");
    }

    private byte[] generateDocxReport(byte[] docxTemplateData, byte[] jsonData,
            String format) {
        String exportFormat = normalizeFormat(format);
        if (!Constant.FORMAT_WORD.equals(exportFormat)
                && !Constant.EXTENSION_PDF.equals(exportFormat)) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "format");
        }
        Object renderData = resolveDocxRenderData(jsonData);
        try (InputStream templateStream = new ByteArrayInputStream(
                docxTemplateData);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                XWPFTemplate template = XWPFTemplate.compile(templateStream)) {
            template.render(renderData);
            if (Constant.EXTENSION_PDF.equals(exportFormat)) {
                return convertRenderedDocxToPdf(template);
            }
            template.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private byte[] convertRenderedDocxToPdf(XWPFTemplate template) {
        try (ByteArrayOutputStream docxOutputStream =
                new ByteArrayOutputStream();
                ByteArrayOutputStream pdfOutputStream =
                        new ByteArrayOutputStream()) {
            template.write(docxOutputStream);
            convertDocxBytesToPdf(docxOutputStream.toByteArray(),
                    pdfOutputStream);
            return pdfOutputStream.toByteArray();
        } catch (IOException ex) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void convertDocxBytesToPdf(byte[] docxBytes,
            ByteArrayOutputStream pdfOutputStream) {
        try (InputStream docxInputStream = new ByteArrayInputStream(docxBytes);
                XWPFDocument document = new XWPFDocument(docxInputStream)) {
            PdfOptions options = PdfOptions.create();
            PdfConverter.getInstance().convert(document, pdfOutputStream,
                    options);
        } catch (Exception ex) {
            throw new BusinessException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public ComCfgTemplate getComCfgTemplateEntity(String templateCode) {
        return comCfgTemplateRepository.findEntityByTemplateCode(templateCode)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_EXIST_TEMPLATE_CODE, templateCode));
    }

    private String updateFile(ComCfgTemplate entity, MultipartFile file, String prefixPath, boolean isMappingFile) {
        minioService.uploadFile(prefixPath, file);
        double fileSizeInMb = (double) file.getSize() / (1024 * 1024);
        BigDecimal roundedSize = BigDecimal.valueOf(fileSizeInMb).setScale(3, RoundingMode.HALF_UP);
        if (isMappingFile) {
            return file.getOriginalFilename();
        }
        entity.setFileSize(roundedSize);
        return file.getOriginalFilename();
    }

    private void validateTemplateCode(String templateCode) {
        if (templateCode == null || templateCode.isBlank()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "templateCode");
        }
    }

    private String normalizeFormat(String format) {
        if (format == null || format.isBlank()) {
            return Constant.EXTENSION_PDF;
        }
        String normalized = format.trim().toLowerCase(Locale.ROOT);
        if (Constant.FORMAT_WORD.equals(normalized)
                || Constant.FORMAT_DOCX.equals(normalized)) {
            return Constant.FORMAT_WORD;
        }
        if (Constant.FORMAT_XLSX.equals(normalized)
                || Constant.FORMAT_EXCEL.equals(normalized)) {
            return Constant.FORMAT_XLSX;
        }
        if (Constant.EXTENSION_PDF.equals(normalized)) {
            return Constant.EXTENSION_PDF;
        }
        throw new BusinessException(CommonErrorCode.BAD_REQUEST, "format");
    }

    private Object resolveDocxRenderData(byte[] jsonData) {
        try {
            var jsonNode = objectMapper.readTree(jsonData);
            if (jsonNode == null || jsonNode.isNull()) {
                return Map.of();
            }
            if (jsonNode.isObject()) {
                return objectMapper.convertValue(jsonNode, Map.class);
            }
            if (jsonNode.isArray()) {
                return Map.of("data", objectMapper.convertValue(jsonNode, List.class));
            }
            return Map.of("value", objectMapper.convertValue(jsonNode, Object.class));
        } catch (IOException ex) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "dataSource");
        }
    }

    private byte[] downloadTemplateFile(ComCfgTemplate entity,
            String fileName) {
        String filePath = Utils.buildTemplatePath(entity.getTemplateCode())
                + fileName;
        return minioService.downloadFile(filePath);
    }

    private byte[] resolveJsonData(ReportReqDto request,
            ComCfgTemplate entity) {
        Object dataSource = request.getDataSource();
        if (dataSource != null) {
            try {
                return objectMapper.writeValueAsBytes(dataSource);
            } catch (IOException ex) {
                throw new BusinessException(CommonErrorCode.BAD_REQUEST, "dataSource");
            }
        }
        String mappingFilePath = entity.getMappingFilePath();
        if (mappingFilePath == null || mappingFilePath.isBlank()) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST, "mappingFilePath");
        }
        return downloadTemplateFile(entity, mappingFilePath);
    }
}
