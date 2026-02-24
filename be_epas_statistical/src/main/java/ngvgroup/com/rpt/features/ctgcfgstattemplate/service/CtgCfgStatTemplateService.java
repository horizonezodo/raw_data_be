package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstattemplate.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface CtgCfgStatTemplateService {
    List<TreeData> getTreeData();
    Page<CtgCfgStatTemplateDtoV3> pageTemplate(String keyword, List<String> templateGroupCodes, List<String> circularCodes, Pageable pageable);
    ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels,  Map<String,List<String>> frequenceMap, String fileName);
    void deleteTemplate(String templateCode);
    CtgCfgStatTemplateDto getOneTemplate(String templateCode);
    void createTemplate(CreateFormDTO dto, MultipartFile templateFile, MultipartFile templateReportFile, MultipartFile userGuideFile);
    Page<CtgCfgStatTemplateDtoV1> getPageTemplateGroupCode(String keyword, Pageable pageable);
    void updateTemplate(CreateFormDTO dto, String templateCode,MultipartFile templateFile, MultipartFile templateReportFile, MultipartFile userGuideFile);

    CreateFormDTO getDetail(String templateCode);


    // Report
    Page<ReportRuleDto> getListReportRule(FilterReportRuleDto filter , String search , Pageable pageable);
    List<CtgCfgStatTemplateDtoV1> findALlTemplate();
    List<TreeData> getTreeDataV2();
}
