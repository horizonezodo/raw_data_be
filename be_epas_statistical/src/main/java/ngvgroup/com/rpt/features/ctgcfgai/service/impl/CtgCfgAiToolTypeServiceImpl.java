package ngvgroup.com.rpt.features.ctgcfgai.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.CtgCfgAiToolTypeDTO;
import ngvgroup.com.rpt.features.ctgcfgai.dto.ctgcfgaitooltype.ListAIToolTypeExcel;
import ngvgroup.com.rpt.features.ctgcfgai.mapper.CtgCfgAiToolTypeMapper;
import ngvgroup.com.rpt.features.ctgcfgai.model.CtgCfgAiToolType;
import ngvgroup.com.rpt.features.ctgcfgai.repository.CtgCfgAiToolTypeRepository;
import ngvgroup.com.rpt.features.ctgcfgai.service.CtgCfgAiToolTypeService;
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
public class CtgCfgAiToolTypeServiceImpl implements CtgCfgAiToolTypeService {
    private final CtgCfgAiToolTypeRepository repository;
    private final CtgCfgAiToolTypeMapper mapper;
    private final ExcelService excelService;

    @Override
    public void createAiTool(CtgCfgAiToolTypeDTO dto) {
        checkData(dto);
        existsToolAiType(dto.getToolAiTypeCode());
        CtgCfgAiToolType type = mapper.toEntity(dto);
        type.setOrgCode(VariableConstants.ORG);
        type.setIsActive(dto.getActive());
        type.setRecordStatus(VariableConstants.DD);
        repository.save(type);
    }

    @Override
    public void updateAiTool(CtgCfgAiToolTypeDTO dto, String toolAiTypeCode) {
        checkData(dto);
        CtgCfgAiToolType type = this.getOne(toolAiTypeCode);
        mapper.updateEntityFromDto(dto,type);
        type.setIsActive(dto.getActive());
        type.setModifiedDate(type.getModifiedDate());
        repository.save(type);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteAiTool(String toolAiTypeCode) {
        CtgCfgAiToolType type = this.getOne(toolAiTypeCode);
        repository.delete(type);
    }

    @Override
    public Page<CtgCfgAiToolTypeDTO> page(String keyword, Pageable pageable) {
        return repository.getPageAITool(keyword, pageable);
    }

    @Override
    public CtgCfgAiToolTypeDTO getDetail(String toolAiTypeCode) {
        CtgCfgAiToolType type = this.getOne(toolAiTypeCode);
        CtgCfgAiToolTypeDTO dto = mapper.toDto(type);
        dto.setActive(type.getIsActive());
        return dto;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName) {
        List<ListAIToolTypeExcel> lst = this.repository.listApiTool(keyword);
        return this.excelService.exportToExcelV2(lst,labels, ListAIToolTypeExcel.class,fileName);
    }

    @Override
    public List<CtgCfgAiToolTypeDTO> listAiTool() {
        return mapper.toListDto(repository.findByIsActiveTrue());
    }

    private CtgCfgAiToolType getOne(String toolAiTypeCode){
        CtgCfgAiToolType type = repository.findByToolAiTypeCode(toolAiTypeCode);
        if(type == null) throw new BusinessException(StatisticalErrorCode.AI_TYPE_NOT_FOUND);
        return type;
    }

    private boolean checkText(String txt){
       return txt.isBlank() || txt.isEmpty();
    }

    private void checkData(CtgCfgAiToolTypeDTO dto){
        if(dto == null) throw new BusinessException(StatisticalErrorCode.TOOL_AI_TYPE_INVALID);
        if(checkText(dto.getToolAiTypeCode()))throw new BusinessException(StatisticalErrorCode.TOOL_AI_TYPE_INVALID);
        if(checkText(dto.getToolAiTypeCode())) throw new BusinessException(StatisticalErrorCode.TOOL_AI_TYPE_NAME_INVALID);
    }

    private void existsToolAiType(String toolAiTypeCode){
        if(repository.existsByToolAiTypeCode(toolAiTypeCode)) throw new BusinessException(StatisticalErrorCode.TOOL_AI_TYPE_CONFIG);
    }
}
