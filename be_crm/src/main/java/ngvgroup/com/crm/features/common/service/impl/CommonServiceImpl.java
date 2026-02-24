package ngvgroup.com.crm.features.common.service.impl;

import java.util.List;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.crm.features.common.dto.*;
import ngvgroup.com.crm.features.common.repository.*;
import ngvgroup.com.crm.features.common.service.CommonService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Tối ưu hiệu năng cho các thao tác chỉ đọc
public class CommonServiceImpl implements CommonService {

    private final ComCfgCommonRepository comCfgCommonRepository;
    private final ComInfAreaRepository comInfAreaRepository;
    private final ComInfEconomicTypeRepository comInfEconomicTypeRepository;
    private final ComInfIndustryRepository comInfIndustryRepository;
    private final ComInfProvinceRepository comInfProvinceRepository;
    private final ComInfWardRepository comInfWardRepository;
    private final ComInfOrganizationRepository comInfOrganizationRepository;
    private final ComCfgTemplateRepository comCfgTemplateRepository;

    @Override
    public List<ComCfgCommonDto> getCommonConfigsByType(String commonTypeCode) {
        return comCfgCommonRepository.findDtoByCommonTypeCode(commonTypeCode);
    }

    @Override
    public List<ComInfAreaDto> getAreasByOrg(String orgCode) {
        return comInfAreaRepository.findDtoByOrgCode(orgCode);
    }

    @Override
    public List<ComInfProvinceDto> getAllProvinces() {
        return comInfProvinceRepository.findAllDto();
    }

    @Override
    public List<ComInfWardDto> getWardsByProvince(String provinceCode) {
        return comInfWardRepository.findDtoByProvinceCode(provinceCode);
    }

    @Override
    public List<ComInfEconomicTypeDto> getAllEconomicTypes() {
        return comInfEconomicTypeRepository.findAllDto();
    }

    @Override
    public List<ComInfIndustryDto> getAllIndustries() {
        return comInfIndustryRepository.findAllDto();
    }

    @Override
    public List<ComInfOrganizationDto> getAllOrganizations() {
        return comInfOrganizationRepository.getAllOrganizations();
    }

    @Override
    public List<ComCfgCommonDto> getCommonConfigsByTypeAndParentCode(String commonTypeCode, String parentCode) {
        return comCfgCommonRepository.findDtoByCommonTypeCodeAndParentCode(commonTypeCode, parentCode);
    }

    @Override
    public Page<TemplateResDto> searchAllTemplate(String keyword, Pageable pageable) {
        return comCfgTemplateRepository.searchAllTemplate(keyword, pageable);
    }

    @Override
    public TemplateResDto getTemplateByCode(String templateCode) {
        TemplateResDto dto = comCfgTemplateRepository.findByTemplateCode(templateCode).orElseThrow(()
                -> new BusinessException(ErrorCode.NOT_FOUND, "TemplateCode " + templateCode));
        dto.setFilePath(String.format("template/%s/%s", dto.getTemplateCode(), dto.getFilePath()));
        dto.setMappingFilePath(String.format("template/%s/%s", dto.getTemplateCode(), dto.getMappingFilePath()));
        return dto;

    }

    @Override
    public List<ComInfWardDto> getWardsByOrg(String orgCode) {
        return comInfWardRepository.findAllByOrgCode(orgCode);
    }

    @Override
    public List<ComInfAreaDto> getAreasByWard() {
        return comInfAreaRepository.findAllDto();
    }
    
}