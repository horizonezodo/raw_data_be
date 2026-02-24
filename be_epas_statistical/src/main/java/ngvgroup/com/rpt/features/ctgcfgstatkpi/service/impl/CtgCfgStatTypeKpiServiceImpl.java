package ngvgroup.com.rpt.features.ctgcfgstatkpi.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import ngvgroup.com.rpt.core.constant.StatisticalErrorCode;
import ngvgroup.com.rpt.core.constant.VariableConstants;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.mapper.CtgCfgStatTypeKpiMapper;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatTypeKpi;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.repository.CtgCfgStatTypeKpiRepository;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.service.CtgCfgStatTypeKpiService;
import ngvgroup.com.rpt.features.excel.service.ExcelService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CtgCfgStatTypeKpiServiceImpl implements CtgCfgStatTypeKpiService {
    private final CtgCfgStatTypeKpiRepository ctgCfgStatTypeKpiRepository;
    private final CtgCfgStatTypeKpiMapper ctgCfgStatTypeKpiMapper;
    private final ExcelService excelService;

    @Override
    public List<CtgCfgStatTypeKpiDto> getAll() {
        List<CtgCfgStatTypeKpi> ctgCfgStatTypeKpis = ctgCfgStatTypeKpiRepository.findAll();

        return ctgCfgStatTypeKpis.stream()
                .map(CtgCfgStatTypeKpiMapper.INSTANCE::toDto)
                .toList();
    }

    @Override
    public Page<CtgCfgStatTypeKpiDto> pageTypeKpi(String keyword, Pageable pageable) {
        return this.ctgCfgStatTypeKpiRepository.pageTypeKpi(keyword, pageable);
    }

    @Override
    public void createTypeKpi(CtgCfgStatTypeKpiDto dto) {
        this.validateData(dto);
        CtgCfgStatTypeKpi kpi = this.ctgCfgStatTypeKpiMapper.toEntity(dto);
        kpi.setOrgCode(VariableConstants.ORG);
        kpi.setRecordStatus(VariableConstants.DD);
        this.ctgCfgStatTypeKpiRepository.save(kpi);
    }

    @Override
    public void updateTypeKpi(CtgCfgStatTypeKpiDto dto, String kpiTypeCode) {
        CtgCfgStatTypeKpi type = this.getOneTypeKpi(kpiTypeCode);
        this.ctgCfgStatTypeKpiMapper.updateEntityFromDto(dto,type);
        this.ctgCfgStatTypeKpiRepository.save(type);
    }

    @Override
    @Transactional
    @Modifying
    public void deleteTypeKpi(String kpiTypeCode) {
        CtgCfgStatTypeKpi type = this.getOneTypeKpi(kpiTypeCode);
        this.ctgCfgStatTypeKpiRepository.delete(type);
    }

    @Override
    public CtgCfgStatTypeKpiDto getOne(String kpiTypeCode) {
        return this.ctgCfgStatTypeKpiMapper.toDto(this.getOneTypeKpi(kpiTypeCode));
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(List<String> labels, String keyword, String fileName) {
        List<CtgCfgStatTypeKpiDto> lst = this.ctgCfgStatTypeKpiRepository.listTypeKpi(keyword);
        return this.excelService.exportToExcelV2(lst,labels,CtgCfgStatTypeKpiDto.class,fileName);
    }

    private CtgCfgStatTypeKpi getOneTypeKpi(String kpiTypeCode){
        CtgCfgStatTypeKpi type = this.ctgCfgStatTypeKpiRepository.findByKpiTypeCode(kpiTypeCode);
        if(type == null) throw new BusinessException(StatisticalErrorCode.TYPE_KPI_NOT_FOUND);
        return type;
    }

    private void validateData(CtgCfgStatTypeKpiDto dto){
        if(dto == null) throw new BusinessException(StatisticalErrorCode.TYPE_KPI_TYPE_IS_EMPTY);
    }

    @Override
    public Boolean checkExistStatScoreGroupCode(String kpiTypeCode) {
        return ctgCfgStatTypeKpiRepository.existsByKpiTypeCode(kpiTypeCode);
    }

}
