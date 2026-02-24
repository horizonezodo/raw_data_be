package com.naas.admin_service.features.setting.service.impl;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.core.contants.EntityStatus;
import com.naas.admin_service.features.setting.dto.ComCfgParameterDto;
import com.naas.admin_service.features.setting.dto.ComCfgParameterResponse;
import com.naas.admin_service.features.setting.mapper.ComCfgParameterMapper;
import com.naas.admin_service.features.setting.model.ComCfgParameter;
import com.naas.admin_service.features.setting.repository.ComCfgParameterRepository;
import com.naas.admin_service.features.setting.service.ComCfgParameterService;
import com.naas.admin_service.core.excel.dto.request.ExportExcelRequest;
import com.naas.admin_service.core.excel.dto.request.SearchFilterRequest;
import com.naas.admin_service.features.category.dto.cominforganization.ComInfOrganizationDto;
import com.naas.admin_service.features.category.repository.ComInfOrganizationRepository;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComCfgParameterServiceImpl implements ComCfgParameterService {

    private final ComCfgParameterRepository parameterRepository;
    private final ComCfgParameterMapper parameterMapper;
    private final ExcelService excelService;
    private final ComInfOrganizationRepository repo;

    @Override
    @Transactional
    public void createComCfgParameter(ComCfgParameterDto request) {
        ComCfgParameter comCfgParameter = parameterMapper.toEntity(request);
        comCfgParameter.setRecordStatus(Constant.APPROVAL);
        parameterRepository.save(comCfgParameter);
    }

    @Override
    @Transactional
    public void updateComCfgParameter(Long id, ComCfgParameterDto request) {

        ComCfgParameter comCfgParameter = parameterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "tham số hệ thống " + id));
        parameterMapper.updateEntity(request, comCfgParameter);
        parameterRepository.save(comCfgParameter);
    }

    @Override
    @Transactional
    public void deleteComCfgParameter(Long id) {
        ComCfgParameter comCfgParameter = parameterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "tham số hệ thống " + id));
        comCfgParameter.setIsDelete(EntityStatus.IsDelete.DELETED.getValue());
        comCfgParameter.setIsActive(EntityStatus.IsActive.NOT_ACTIVE.getValue());
        parameterRepository.save(comCfgParameter);
    }

    @Override
    public ComCfgParameterDto findComCfgParameterById(Long id) {
        return parameterRepository.findById(id)
                .map(parameterMapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "tham số hệ thống với id" + id));
    }

    @Override
    public ComCfgParameterDto findComCfgParameterByCode(String paramCode) {
        return parameterRepository.findByParamCodeAndIsActiveTrueAndIsDeleteFalse(paramCode)
                .map(parameterMapper::toDto)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "tham số hệ thống với mã " + paramCode));
    }

    @Override
    public List<ComCfgParameterResponse> listComCfgParameter(SearchFilterRequest request) {
        return this.listComCfgParameters(request);
    }

    //chuyển qua dùng feign  lấy list org kết hợp với list ComCfgParameterResponse để tạo 1 list response hoàn chỉnh
    private List<ComCfgParameterResponse> listComCfgParameters(SearchFilterRequest request){
        List<ComInfOrganizationDto> lstOrg = this.getListOrg(request.getFilter());
        List<ComCfgParameterResponse> lstParam = this.parameterRepository.listComCfgParameters(request.getFilter());

        // Map orgCode -> orgName
        Map<String, String> orgCodeToNameMap = lstOrg.stream()
                .collect(Collectors.toMap(ComInfOrganizationDto::getOrgCode, ComInfOrganizationDto::getOrgName));

        // Map param với orgName tương ứng
        return lstParam.stream()
                .map(param -> {
                    String orgName;
                    if ("%".equals(param.getOrgCode())) {
                        orgName = "Tất cả";
                    } else {
                        orgName = orgCodeToNameMap.getOrDefault(param.getOrgCode(), null);
                    }
                    param.setOrgName(orgName);
                    return param;
                })
                .distinct()
                .toList();
    }

    private List<ComInfOrganizationDto> getListOrg(String keyword){
        return repo.listOrganization(keyword);
    }
    //chuyển qua dùng cái mới
    @Override
    public ResponseEntity<byte[]> exportExcel(ExportExcelRequest request) {
        List<ComCfgParameterResponse> parameters = this.listComCfgParameters(request);
        if (parameters.isEmpty()) {
            throw new BusinessException(ErrorCode.NOT_FOUND, " tham số hệ thống");
        }
        String fileName = "Danh_sach_tham_so_he_thong";
        byte[] response = excelService.exportToExcelContent(parameters, request.getLabels(), ComCfgParameterResponse.class);
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + ".xlsx\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(response);
        } catch (Exception e) {
            log.error("Error: Lỗi tạo file Excel {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ComCfgParameterDto> getAllByCodeAndValue(String paramCode, String paramValue) {
        List<ComCfgParameter> lst = parameterRepository.findAllByParamCodeAndParamValue(paramCode,paramValue);
        return parameterMapper.mapToDTO(lst);
    }

    public String getCurrentToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getTokenValue();
        }
        return null;
    }
}
