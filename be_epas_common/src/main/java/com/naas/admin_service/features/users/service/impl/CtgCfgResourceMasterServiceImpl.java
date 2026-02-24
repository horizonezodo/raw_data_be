package com.naas.admin_service.features.users.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.core.utils.PageUtils;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemapping.ListResourceMappingDto;
import com.naas.admin_service.features.users.dto.ctgcfgresourcemaster.*;
import com.naas.admin_service.features.users.mapper.ComCfgResourceMasterMapper;
import com.naas.admin_service.features.users.model.CtgCfgResourceMapping;
import com.naas.admin_service.features.users.model.CtgCfgResourceMaster;
import com.naas.admin_service.features.users.repository.CtgCfgResourceMappingRepository;
import com.naas.admin_service.features.users.repository.CtgCfgResourceMasterRepository;
import com.naas.admin_service.features.users.service.CtgCfgResourceMasterService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
public class CtgCfgResourceMasterServiceImpl extends BaseStoredProcedureService implements CtgCfgResourceMasterService {

    private final CtgCfgResourceMasterRepository ctgCfgResourceMasterRepository;
    private final ComCfgResourceMasterMapper comCfgResourceMasterMapper;
    private final CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ExcelService excelService;

    public CtgCfgResourceMasterServiceImpl(CtgCfgResourceMasterRepository ctgCfgResourceMasterRepository,
                                           ComCfgResourceMasterMapper comCfgResourceMasterMapper,
                                           CtgCfgResourceMappingRepository ctgCfgResourceMappingRepository,
                                           JdbcTemplate jdbcTemplate,
                                           ExcelService excelService
    ) {
        super();
        this.ctgCfgResourceMasterRepository = ctgCfgResourceMasterRepository;
        this.comCfgResourceMasterMapper = comCfgResourceMasterMapper;
        this.ctgCfgResourceMappingRepository = ctgCfgResourceMappingRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.excelService = excelService;
    }

    @Override
    public void createCfgResourceMaster(CtgCfgResourceMasterDto ctgCfgResourceMasterDto) {
        boolean isExist = ctgCfgResourceMasterRepository.existsByResourceTypeCode(ctgCfgResourceMasterDto.getResourceTypeCode());
        if (isExist) {
            throw new BusinessException(CommonErrorCode.RESOURCE_TYPE_ALREADY_EXISTS);
        }
        CtgCfgResourceMaster ctgCfgResourceMaster = comCfgResourceMasterMapper.toEntity(ctgCfgResourceMasterDto);
        ctgCfgResourceMaster.setIsActive(1);
        ctgCfgResourceMaster.setRecordStatus(Constant.APPROVAL);
        ctgCfgResourceMasterRepository.save(ctgCfgResourceMaster);
    }

    @Override
    public void updateCfgResourceMaster(Long id, CtgCfgResourceMasterDto ctgCfgResourceMasterDto) {


        CtgCfgResourceMaster ctgCfgResourceMaster = ctgCfgResourceMasterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, "không tìm thấy loại tài nguyên với id =  " + id));
        if (!ctgCfgResourceMasterDto.getResourceTypeCode().equals(ctgCfgResourceMaster.getResourceTypeCode())) {
            boolean isExist = ctgCfgResourceMasterRepository.existsByResourceTypeCode(ctgCfgResourceMasterDto.getResourceTypeCode());
            if (isExist) {
                throw new BusinessException(CommonErrorCode.RESOURCE_TYPE_ALREADY_EXISTS);
            }
        }
        ctgCfgResourceMaster.setResourceTypeCode(ctgCfgResourceMasterDto.getResourceTypeCode());
        ctgCfgResourceMaster.setResourceTypeName(ctgCfgResourceMasterDto.getResourceTypeName());
        ctgCfgResourceMaster.setResourceSql(ctgCfgResourceMasterDto.getResourceSql());


        ctgCfgResourceMasterRepository.save(ctgCfgResourceMaster);
    }

    @Override
    public CtgCfgResourceMasterDto getCfgResourceMasterById(Long id) {

        CtgCfgResourceMaster ctgCfgResourceMaster = ctgCfgResourceMasterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, "không tìm thấy loại tài nguyên với id= " + id));

        return comCfgResourceMasterMapper.toDto(ctgCfgResourceMaster);
    }

    @Override
    public void deleteCfgResourceMasterById(Long id) {

        CtgCfgResourceMaster ctgCfgResourceMaster = ctgCfgResourceMasterRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonErrorCode.NOT_FOUND, "không tìm thấy loại tài nguyên với id= " + id));

        boolean isExistInComCfgResourceMapping = ctgCfgResourceMappingRepository
                .existsComCfgResourceMappingByResourceTypeCode(ctgCfgResourceMaster.getResourceTypeCode());

        if (isExistInComCfgResourceMapping) {
            throw new BusinessException(CommonErrorCode.BAD_REQUEST,
                    "không thể xóa loại tài nguyên " + ctgCfgResourceMaster.getResourceTypeName());
        }

        ctgCfgResourceMasterRepository.deleteById(id);
    }

    @Override
    public Page<CtgCfgResourceMasterDto> searchCfgResourceMaster(SearchDTO req) {

        Pageable pageable = PageUtils.toPageable(req.getPageable());

        return ctgCfgResourceMasterRepository.searchComCfgResourceMaster(req.getFilter(), pageable);
    }

    /**
     * Parse danh sách tên cột trong câu query dạng SELECT ... FROM ...
     */
    private List<String> getSelectedColumnsFromSql(String sql) {
        String upperSql = sql.toUpperCase();
        int selectIndex = upperSql.indexOf("SELECT");
        int fromIndex = upperSql.indexOf("FROM");

        if (selectIndex == -1 || fromIndex == -1 || fromIndex < selectIndex) {
            throw new IllegalArgumentException("Invalid SQL syntax: missing SELECT or FROM in SQL: " + sql);
        }
        String columnPart = sql.substring(selectIndex + 6, fromIndex).trim();

        String[] columns = columnPart.split(",");

        List<String> result = new ArrayList<>();
        for (String col : columns) {
            result.add(col.trim());
        }
        return result;
    }


    @Override
    public Page<CtgCfgResourceMasterDtoV2> getAllComCfgResourceMasterActive(SearchResourceRequest searchDTO) {
        Pageable pageable = PageUtils.toPageable(searchDTO.getPageable());
        return ctgCfgResourceMappingRepository
                .findByUserIdAndResourceTypeCodeAndIsActiveEffective(getCurrentUserId(), searchDTO.getFilter(), 1, pageable, searchDTO.getFiledSearch())
                .map(comCfgResourceMasterMapper::toDtoV2);

    }

    private List<CtgCfgResourceMasterDtoV2> getListComCfgResourceMasterActive(SearchResourceRequest searchDTO,String userId) {
        return ctgCfgResourceMappingRepository
                .findAllByUserIdAndResourceTypeCodeAndIsActiveEffective(userId, searchDTO.getFilter(), 1, searchDTO.getFiledSearch())
                .stream()
                .map(entity -> {
                    CtgCfgResourceMasterDtoV2 dto = comCfgResourceMasterMapper.toDtoV2(entity);
                    dto.setCheck(true);
                    return dto;
                })
                .toList();
    }

    private List<CtgCfgResourceMasterDtoV2> getListGroupComCfgResourceMasterActive(SearchResourceRequest searchDTO,String groupId) {
        return ctgCfgResourceMappingRepository
                .findAllByGroupIdAndResourceTypeCodeAndIsActiveEffective(groupId, searchDTO.getFilter(), 1, searchDTO.getFiledSearch())
                .stream()
                .map(entity -> {
                    CtgCfgResourceMasterDtoV2 dto = comCfgResourceMasterMapper.toDtoV2(entity);
                    dto.setCheck(true);
                    return dto;
                })
                .toList();
    }

    @Override
    public List<CtgCfgResourceMasterDtoV2> getAllResource(Long id, SearchResourceRequest searchDTO, String userId) {
        return this.getAllResources(id,searchDTO,null,userId);
    }

    @Override
    public List<CtgCfgResourceMasterDtoV2> getAllGroupResource(Long id, SearchResourceRequest searchDTO, String groupId) {
        return this.getAllResources(id,searchDTO,groupId,null);
    }


    private List<CtgCfgResourceMasterDtoV2> getAllResources(Long id, SearchResourceRequest searchDTO, String groupId, String userId) {
        CtgCfgResourceMasterDto ctgCfgResourceMasterDto = getCfgResourceMasterById(id);
        String resourceSql = ctgCfgResourceMasterDto.getResourceSql();

        ResourceSelection selection = getResourceSelection(searchDTO, groupId, userId, ctgCfgResourceMasterDto.getResourceTypeCode());
        List<String> selectedColumns = validateAndGetSelectedColumns(resourceSql, ctgCfgResourceMasterDto.getResourceTypeCode());
        List<CtgCfgResourceMasterDtoV2> listNoChoice = buildAvailableResources(resourceSql, selectedColumns, selection.existingMappings());

        List<CtgCfgResourceMasterDtoV2> responseList = new ArrayList<>(listNoChoice.size() + selection.listChoice().size());
        responseList.addAll(listNoChoice);
        responseList.addAll(selection.listChoice());

        return responseList;
    }

    private ResourceSelection getResourceSelection(SearchResourceRequest searchDTO, String groupId, String userId, String resourceTypeCode) {
        if (groupId != null) {
            List<CtgCfgResourceMasterDtoV2> listChoice = this.getListGroupComCfgResourceMasterActive(searchDTO, groupId);
            List<String> existingMappings = ctgCfgResourceMappingRepository.findByGroupIdAndResourceTypeCodeAndIsActiveEffective(groupId, resourceTypeCode, 1)
                    .stream()
                    .map(CtgCfgResourceMapping::getResourceCode)
                    .toList();
            return new ResourceSelection(listChoice, existingMappings);
        }
        List<CtgCfgResourceMasterDtoV2> listChoice = this.getListComCfgResourceMasterActive(searchDTO, userId);
        List<String> existingMappings = ctgCfgResourceMappingRepository.findByUserIdAndResourceTypeCodeAndIsActiveEffective(userId, resourceTypeCode, 1)
                .stream()
                .map(CtgCfgResourceMapping::getResourceCode)
                .toList();
        return new ResourceSelection(listChoice, existingMappings);
    }

    private List<String> validateAndGetSelectedColumns(String resourceSql, String resourceTypeCode) {
        List<String> selectedColumns = getSelectedColumnsFromSql(resourceSql);
        if (selectedColumns.isEmpty()) {
            throw new BusinessException(CommonErrorCode.INVALID_DATA_ENTITY, "No columns found in SQL: " + resourceSql);
        }
        if (!isValidateSql(resourceSql)) {
            throw new BusinessException(CommonErrorCode.INVALID_DATA_ENTITY, resourceTypeCode);
        }
        return selectedColumns;
    }

    private List<CtgCfgResourceMasterDtoV2> buildAvailableResources(String resourceSql, List<String> selectedColumns, List<String> existingMappings) {
        return jdbcTemplate.queryForList(resourceSql).stream()
                .map(row -> mapRowToDto(selectedColumns, row))
                .filter(item -> !existingMappings.contains(item.getResourceCode()))
                .toList();
    }

    private CtgCfgResourceMasterDtoV2 mapRowToDto(List<String> selectedColumns, Map<String, Object> row) {
        String name = null;
        String code = null;
        String desc = null;

        for (String column : selectedColumns) {
            Object value = row.get(column);
            if (value == null) {
                continue;
            }
            String colUpper = column.toUpperCase();
            if (colUpper.contains("NAME")) {
                name = value.toString();
            } else if (colUpper.contains("CODE")) {
                code = value.toString();
            } else if (desc == null) {
                desc = value.toString();
            }
        }
        return new CtgCfgResourceMasterDtoV2(code, desc, name, false);
    }

    private record ResourceSelection(List<CtgCfgResourceMasterDtoV2> listChoice, List<String> existingMappings) {}


    @Override
    public ResponseEntity<ByteArrayResource> exportExcel(ExportExcelReq request) {
        String fileName = "Danh_sach_loai_tai_nguyen";

        List<CtgCfgResourceMasterDto> ctgCfgResourceMasterDtos = ctgCfgResourceMasterRepository.searchComCfgResourceMaster(request.getFilter());

        if (ctgCfgResourceMasterDtos.isEmpty()) {
            throw new BusinessException(CommonErrorCode.NOT_FOUND, "loại tài nguyên.");
        }
        return excelService.exportToExcel(ctgCfgResourceMasterDtos, request.getLabels(), CtgCfgResourceMasterDto.class, fileName);
    }

    private boolean isValidateSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) return false;
        String finalSql = sql.trim().toLowerCase();
        int selectCount = finalSql.split("select", finalSql.length()).length - 1;
        if (selectCount > 1) return false;
        // Regex để kiểm tra cú pháp SELECT cơ bản

        // Regex an toàn hơn: tránh backtracking bằng cách giới hạn ký tự, không dùng DOTALL
        final Pattern sqlPattern = Pattern.compile("(?is)^\\s*select\\s+[^;\\r\\n]+\\s+from\\s+[^;\\r\\n]+\\s*$");

        // block keyword
        final String[] blockedKeywords = {
                " insert", " update", " delete", " drop", " truncate", " exec", " alter", " create", " union", " intersect", " except", " with"
        };

        for (String keyword : blockedKeywords) {
            if (finalSql.contains(keyword)) {
                return false;
            }
        }
        return sqlPattern.matcher(sql).matches();
    }

    @Override
    public List<ListResourceMappingDto> getListBranch(String userId) {
        if (userId == null) {
            userId = getCurrentUserId();
        }
        return this.ctgCfgResourceMappingRepository.findAllListResourceMappingDto(userId);
    }
}
