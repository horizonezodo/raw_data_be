package ngvgroup.com.rpt.features.ctgcfgai.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTO;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV1;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.CtgCfgAiToolDTOV2;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitool.ExportExcelData;
import ngvgroup.com.rpt.features.ctgcfgai.mapper.CtgCfgAiToolMapper;
import ngvgroup.com.rpt.features.ctgcfgai.model.CtgCfgAiTool;
import ngvgroup.com.rpt.features.ctgcfgai.repository.CtgCfgAiToolRepository;
import ngvgroup.com.rpt.features.ctgcfgai.service.CtgCfgAiToolService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgCfgAiToolServiceImpl implements CtgCfgAiToolService {
    private final CtgCfgAiToolRepository repo;
    private final CtgCfgAiToolMapper mapper;
    private final ExcelService service;

    @Override
    public Page<CtgCfgAiToolDTOV1> pageAiTool(String keyword, Pageable pageable) {
        return repo.pageToolAI(keyword, pageable);
    }

    @Override
    public List<CtgCfgAiToolDTOV2> getAllTools() {
        return repo.getAllTools();
    }

    @Override
    public void createAiTool(CtgCfgAiToolDTO dto) {
        this.checkData(dto);
        if(this.checkToolExists(dto.getToolAiCode())) throw new BusinessException(StatisticalErrorCode.AI_TOOL_CODE_CONFLIG);
        CtgCfgAiTool tool = mapper.toEntity(dto);
        tool.setIsActive(dto.getActive());
        tool.setRecordStatus(VariableConstants.DD);
        tool.setOrgCode(VariableConstants.ORG);
        repo.save(tool);
    }

    @Override
    public void updateAiTool(CtgCfgAiToolDTO dto, String toolAiCode) {
        this.checkData(dto);
        CtgCfgAiTool tool = this.getOne(toolAiCode);
        mapper.updateEntityFromDto(dto,tool);
        tool.setIsActive(dto.getActive());
        repo.save(tool);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteAiTool(String toolAiCode) {
        CtgCfgAiTool tool = this.getOne(toolAiCode);
        repo.delete(tool);
    }

    @Override
    public CtgCfgAiToolDTO getDetail(String toolAiCode) {
        CtgCfgAiTool tool = this.getOne(toolAiCode);
        CtgCfgAiToolDTO dto = mapper.toDto(tool);
        dto.setActive(tool.getIsActive());
        return dto;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName) {
        List<ExportExcelData> lst = this.repo.listToolAI(keyword);
        return this.service.exportToExcelV2(lst,labels, ExportExcelData.class,fileName);
    }

    private CtgCfgAiTool getOne(String toolAiCode){
        CtgCfgAiTool tool = repo.findByToolAiCode(toolAiCode);
        if(tool == null) throw new BusinessException(StatisticalErrorCode.AI_TOOL_NOT_FOUND);
        return tool;
    }

    private void checkData(CtgCfgAiToolDTO dto){
        if(dto == null) throw new BusinessException(StatisticalErrorCode.NOT_EMPTY);
        if(checkText(dto.getToolAiCode())) throw new BusinessException(StatisticalErrorCode.TOOL_AI_CODE_INVALID);
        if(checkText(dto.getToolAiName())) throw new BusinessException(StatisticalErrorCode.TOOL_AI_NAME_INVALID);
        if(checkText(dto.getToolAiTypeCode())) throw new BusinessException(StatisticalErrorCode.TOOL_AI_TYPE_INVALID);
        if(checkText(dto.getOutputFormat())) throw new BusinessException(StatisticalErrorCode.OUTPUT_FORMAT_INVALID);
    }

    private boolean checkToolExists(String toolAiCode){
        return repo.existsByToolAiCode(toolAiCode);
    }

    private boolean checkText(String txt){
        return txt.isBlank();
    }
}
