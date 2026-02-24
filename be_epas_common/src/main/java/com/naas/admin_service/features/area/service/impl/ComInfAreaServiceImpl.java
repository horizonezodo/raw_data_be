package com.naas.admin_service.features.area.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.features.category.dto.CtgInfWardDto;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import com.naas.admin_service.features.area.dto.ComInfAreaDto;
import com.naas.admin_service.features.area.dto.ComInfAreaRequestDto;
import com.naas.admin_service.features.area.dto.ComInfAreaResponse;
import com.naas.admin_service.features.area.mapper.ComInfAreaMapper;
import com.naas.admin_service.features.area.model.ComInfArea;
import com.naas.admin_service.features.area.repository.ComInfAreaRepository;
import com.naas.admin_service.features.area.service.ComInfAreaService;
import com.naas.admin_service.features.area_type.model.ComInfAreaType;
import com.naas.admin_service.features.area_type.repository.ComInfAreaTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ComInfAreaServiceImpl implements ComInfAreaService {
    private final ComInfAreaRepository comInfAreaRepository;
    private final ComInfAreaMapper comInfAreaMapper;
    private final ComInfAreaTypeRepository comAreaTypeRepository;
    private final ExportExcel exportExcel;

    @Override
    public Page<ComInfAreaDto> getCtgComAreas(String keyword, String orgCode, Pageable pageable) {
        return comInfAreaRepository.getList(keyword, orgCode, pageable);
    }

    @Override
    public ComInfAreaRequestDto findOne(Long id) {
        return comInfAreaRepository.findById(id)
                .map(comInfAreaMapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
    }

    @Override
    public void create(ComInfAreaRequestDto ctgComAreaRequest) {
        Optional<ComInfAreaType> opt =  comAreaTypeRepository.findByAreaTypeCode(ctgComAreaRequest.getAreaTypeCode());
        if(opt.isEmpty()){
            throw new BusinessException(ErrorCode.NOT_FOUND, ctgComAreaRequest.getAreaTypeCode());
        }

        ComInfArea comInfArea = comInfAreaMapper.toEntity(ctgComAreaRequest);
        comInfArea.setIsActive(1);

        comInfAreaRepository.save(comInfArea);
    }

    @Override
    public void update(Long id, ComInfAreaRequestDto ctgComAreaRequest) {
        ComInfArea comInfArea = comInfAreaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));

        if (ctgComAreaRequest.getAreaTypeCode() != null
                && !ctgComAreaRequest.getAreaTypeCode().equals(comInfArea.getAreaTypeCode())) {
            Optional<ComInfAreaType> opt = comAreaTypeRepository.findByAreaTypeCode(ctgComAreaRequest.getAreaTypeCode());
            if(opt.isEmpty()){
                throw new BusinessException(ErrorCode.NOT_FOUND,ctgComAreaRequest.getAreaTypeCode());
            }
        }

        comInfAreaMapper.updateCtgComAreaFromRequest(ctgComAreaRequest, comInfArea);
        comInfArea.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        comInfAreaRepository.save(comInfArea);
    }

    @Override
    public void delete(Long id) {
        ComInfArea comInfArea = comInfAreaRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, id));
        comInfArea.setIsActive(0);
        comInfArea.setIsDelete(1);
        comInfAreaRepository.save(comInfArea);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(ComInfAreaRequestDto ctgComAreaRequest, String fileName) {
        try {
            List<ComInfAreaResponse> data = comInfAreaRepository.findAllCtgComAreasForExport(
                    ctgComAreaRequest.getOrgCode(),
                    ctgComAreaRequest.getAreaTypeCodes()
            );
            
            String safeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8);
            
            return exportExcel.exportExcel(data, safeFileName);
        }
        catch (Exception e){
            throw new BusinessException(CommonErrorCode.WRITE_EXCEL_ERROR, e);
        }
    }

    @Override
    public Page<ComInfAreaResponse> searchAll(ComInfAreaRequestDto filterRequest, Pageable pageable) {
        if (filterRequest.getFilter() != null) {
            filterRequest.setFilter(filterRequest.getFilter().trim().toLowerCase());
        }
        return comInfAreaRepository.findAllCtgComAreas(filterRequest.getFilter(),filterRequest.getOrgCode(),filterRequest.getAreaTypeCodes(), pageable);
    }

    @Override
    public List<ComInfArea> getCtgComAreasByOrgCodes(List<String> orgCodes) {
        return comInfAreaRepository.findAllByOrgCodes(orgCodes);
    }

    @Override
    public String getByAreaCode(String areaCode) {
        ComInfArea area = comInfAreaRepository.findByAreaCode(areaCode);
        return area.getAreaName();
    }

    @Override
    public List<CtgInfWardDto> getListWard(String orgCode){
        return comInfAreaRepository.getListWard(orgCode);
    }

    @Override
    public List<ComInfAreaDto> getListAreaByWard(){
        return comInfAreaRepository.getListAreaByWard();
    }

    @Override
    public boolean checkExist(String code) {
        List<ComInfAreaDto> entity = comInfAreaRepository.findByAreaCodes(code);
       return !entity.isEmpty();
    }
}
