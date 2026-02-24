package ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.mapper.CtgCfgStatScoreTypeMapper;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreType;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.repository.CtgCfgStatScoreTypeRepository;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.service.CtgCfgStatScoreTypeService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CtgCfgStatScoreTypeServiceImpl implements CtgCfgStatScoreTypeService {
    private final CtgCfgStatScoreTypeRepository ctgCfgStatScoreTypeRepository;
    private final ExcelService excelService;

    @Override
    public Page<CtgCfgStatScoreTypeDto> getAll(String keyword,Pageable pageable){
        return ctgCfgStatScoreTypeRepository.getAll(keyword,pageable);
    }

    @Override
    public List<CtgCfgStatScoreType> listAll() {
        return ctgCfgStatScoreTypeRepository.findAll();
    }

    @Override
    public void create(CtgCfgStatScoreTypeDto ctgCfgStatScoreTypeDto){
        Optional<CtgCfgStatScoreType> ctgCfgStatScoreTypeOptional=ctgCfgStatScoreTypeRepository.findByStatScoreTypeCode(ctgCfgStatScoreTypeDto.getStatScoreTypeCode());
        if(ctgCfgStatScoreTypeOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgStatScoreTypeDto.setOrgCode(VariableConstants.ORG);
        ctgCfgStatScoreTypeDto.setRecordStatus(VariableConstants.DD);
        CtgCfgStatScoreType ctgCfgStatScoreType=CtgCfgStatScoreTypeMapper.INSTANCE.toEntity(ctgCfgStatScoreTypeDto);
        ctgCfgStatScoreTypeRepository.save(ctgCfgStatScoreType);
    }

    @Override
    @Transactional
    public void update(CtgCfgStatScoreTypeDto ctgCfgStatScoreTypeDto){
        Optional<CtgCfgStatScoreType> ctgCfgStatScoreTypeOptional=ctgCfgStatScoreTypeRepository.findByStatScoreTypeCode(ctgCfgStatScoreTypeDto.getStatScoreTypeCode());
        if(!ctgCfgStatScoreTypeOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgStatScoreTypeDto.setId(ctgCfgStatScoreTypeOptional.get().getId());
        ctgCfgStatScoreTypeDto.setOrgCode(VariableConstants.ORG);
        ctgCfgStatScoreTypeDto.setRecordStatus(VariableConstants.DD);
        CtgCfgStatScoreTypeMapper.INSTANCE.updateEntityFromDto(ctgCfgStatScoreTypeDto, ctgCfgStatScoreTypeOptional.get());
        ctgCfgStatScoreTypeRepository.save(ctgCfgStatScoreTypeOptional.get());
    }

    @Override
    public void delete(Long id){
        Optional<CtgCfgStatScoreType> ctgCfgStatScoreTypeOptional=ctgCfgStatScoreTypeRepository.findById(id);
        if(!ctgCfgStatScoreTypeOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgStatScoreTypeRepository.delete(ctgCfgStatScoreTypeOptional.get());
    }

    @Override
    public boolean checkExist(String statScoreTypeCode){
        CtgCfgStatScoreType entity = ctgCfgStatScoreTypeRepository.getByStatScoreTypeCode(statScoreTypeCode);
        return entity != null;
    }

    @Override
    public CtgCfgStatScoreTypeDto getDetail(Long id){
        Optional<CtgCfgStatScoreType>ctgCfgStatScoreTypeOptional = ctgCfgStatScoreTypeRepository.findById(id);
        if(ctgCfgStatScoreTypeOptional.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        return CtgCfgStatScoreTypeMapper.INSTANCE.toDto(ctgCfgStatScoreTypeOptional.get());
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels,String keyword, String fileName){
        List<CtgCfgStatScoreTypeDto> cfgStatScoreTypeDtoList=ctgCfgStatScoreTypeRepository.exportToExcel(keyword);
        return excelService.exportToExcel(cfgStatScoreTypeDtoList,labels,CtgCfgStatScoreTypeDto.class,fileName);
    }

}
