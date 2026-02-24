package ngvgroup.com.rpt.features.ctgcfgstatkpi.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.mapper.CtgCfgStatKpiMapper;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatKpi;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.repository.CtgCfgStatKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.service.CtgCfgStatKpiService;
import ngvgroup.com.rpt.features.ctgcfgstat.service.CtgCfgStatScoreKpiResultService;
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
public class CtgCfgStatKpiServiceImpl implements CtgCfgStatKpiService {
    private final CtgCfgStatKpiRepository ctgCfgStatKpiRepository;
    private final ExcelService excelService;
    private final CtgCfgStatScoreKpiResultService ctgCfgStatScoreKpiResultService;
    private final CtgCfgStatKpiMapper ctgCfgStatKpiMapper;

    @Override
    public Page<CtgCfgStatKpiDto> searchAllStatKpi(String keyword, List<String> kpiTypeCode, Pageable pageable){
        return ctgCfgStatKpiRepository.searchAllStatKpi(keyword,kpiTypeCode,pageable);
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, List<String> kpiTypeCode,String fileName){
        List<CtgCfgStatKpiDto> cfgStatKpiDtoList=ctgCfgStatKpiRepository.exportToExcel(kpiTypeCode);
        return excelService.exportToExcel(cfgStatKpiDtoList,labels,CtgCfgStatKpiDto.class,fileName);
    }

    @Override
    public void create(CtgCfgStatKpiDto ctgCfgStatKpiDto)  throws BusinessException {
        Optional<CtgCfgStatKpi> cfgStatKpiOptional=ctgCfgStatKpiRepository.findByKpiCode(ctgCfgStatKpiDto.getKpiCode());
        if(cfgStatKpiOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgStatKpiDto.setOrgCode(VariableConstants.ORG);
        ctgCfgStatKpiDto.setRecordStatus(VariableConstants.DD);
        ctgCfgStatKpiRepository.save(CtgCfgStatKpiMapper.INSTANCE.toEntity(ctgCfgStatKpiDto));
    }

    @Override
    public CtgCfgStatKpiDto getDetail(Long id) {
        Optional<CtgCfgStatKpi>cfgStatKpiOptional = ctgCfgStatKpiRepository.findById(id);
        if(cfgStatKpiOptional.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        return CtgCfgStatKpiMapper.INSTANCE.toDto(cfgStatKpiOptional.get());
    }

    @Override
    public Page<CtgCfgStatKpiDto> getAllKpiData(String keyword, Pageable pageable) {
        return this.ctgCfgStatKpiRepository.findAllKpiData(keyword,pageable);
    }

    @Override
    @Transactional
    public void update(CtgCfgStatKpiDto ctgCfgStatKpiDto){
        Optional<CtgCfgStatKpi> cfgStatKpiOptional=ctgCfgStatKpiRepository.findByKpiCode(ctgCfgStatKpiDto.getKpiCode());
        if(!cfgStatKpiOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }
        ctgCfgStatKpiDto.setId(cfgStatKpiOptional.get().getId());
        ctgCfgStatKpiDto.setOrgCode(VariableConstants.ORG);
        ctgCfgStatKpiDto.setRecordStatus(VariableConstants.DD);
        CtgCfgStatKpiMapper.INSTANCE.updateEntityFromDto(ctgCfgStatKpiDto, cfgStatKpiOptional.get());
        ctgCfgStatKpiRepository.save(cfgStatKpiOptional.get());

    }

    @Override
    @Transactional
    public void delete(Long id){
        Optional<CtgCfgStatKpi> cfgStatKpiOptional=ctgCfgStatKpiRepository.findById(id);
        if(!cfgStatKpiOptional.isPresent()){
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }

        List<CtgCfgStatKpi> ctgCfgStatKpis=ctgCfgStatKpiRepository.getAllByParentCode(cfgStatKpiOptional.get().getKpiCode());
        if(!ctgCfgStatKpis.isEmpty()){
            throw new BusinessException(StatisticalErrorCode.STAT_KPI_CONFLICT,ctgCfgStatKpis.get(0).getKpiCode()+'-'+ctgCfgStatKpis.get(0).getKpiName());
        }
        ctgCfgStatKpiRepository.delete(cfgStatKpiOptional.get());
        ctgCfgStatScoreKpiResultService.delete((cfgStatKpiOptional.get().getKpiCode()));

    }

    @Override
    public boolean checkExist(String kpiCode){
        CtgCfgStatKpi entity = ctgCfgStatKpiRepository.getByKpiCode(kpiCode);
        return entity != null;
    }

    @Override
    public CtgCfgStatKpiDto getByKpiCode(String kpiCode){
        return ctgCfgStatKpiMapper.toDto(ctgCfgStatKpiRepository.getByKpiCode(kpiCode));
    }


}
