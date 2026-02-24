package com.naas.admin_service.features.category.service.impl;

import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.category.dto.ExportExcelDTO;
import com.naas.admin_service.features.category.dto.CtgInfTitleDTO;
import com.naas.admin_service.features.category.dto.ExportCtgInfTitleDTO;
import com.naas.admin_service.features.category.mapper.CtgInfTitleMapper;
import com.naas.admin_service.features.category.model.CtgInfTitle;
import com.naas.admin_service.features.category.repository.CtgInfTitleRepository;
import com.naas.admin_service.features.category.service.CtgInfTitleService;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgInfTitleServiceImpl extends BaseEntity implements CtgInfTitleService {
    private final CtgInfTitleRepository repo;
    private final CtgInfTitleMapper mapper;
    private final ExcelService service;
    @Override
    public void createTitle(CtgInfTitleDTO dto) {
        CtgInfTitle title = repo.findTitle(dto.getTitleCode(),dto.getTitleName(),1);
        if(title!= null){
            mapper.updateEntityFromDto(dto,title);
            title.setIsDelete(0);
            title.setIsActive(dto.getIsDelete());
            title.setDescription(dto.getDescription());
            repo.save(title);
        }else if(!repo.existsByTitleCode(dto.getTitleCode())){
            CtgInfTitle hrmInfTitle = mapper.toE(dto);
            hrmInfTitle.setIsDelete(0);
            hrmInfTitle.setIsActive(dto.getIsDelete());
            hrmInfTitle.setRecordStatus(Constant.APPROVAL);
            hrmInfTitle.setDescription(dto.getDescription());
            repo.save(hrmInfTitle);
        }
        else{
            throw new BusinessException(ErrorCode.CONFLICT, dto.getTitleCode());
        }
    }

    @Override
    public void updateTitle(String titleCode, CtgInfTitleDTO dto) {
        CtgInfTitle title = repo.findByTitleCode(titleCode);
        if(title !=null){
            mapper.updateEntityFromDto(dto, title);
            title.setIsDelete(0);
            title.setIsActive(dto.getIsDelete());
            title.setDescription(dto.getDescription());
            repo.save(title);
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, titleCode);
        }
    }

    @Transactional
    @Modifying
    @Override
    public void deleteTitle(String titleCode) {
        CtgInfTitle title = repo.findByTitleCode(titleCode);
        if(title !=null){
            repo.delete(title);
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, titleCode);
        }
    }

    @Override
    public Page<CtgInfTitleDTO> search(String keyword, Pageable pageable) {
        return repo.search(keyword,pageable);
    }

    @Override
    public CtgInfTitleDTO getDetail(String titleCode) {
        CtgInfTitle title = repo.findByTitleCode(titleCode);
        if(title !=null){
            return mapper.toDTO(title);
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, titleCode);
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto) {
        List<ExportCtgInfTitleDTO> lst = repo.exportData();
        return service.exportToExcel(lst,dto.getLabels(), ExportCtgInfTitleDTO.class,dto.getFileName());
    }

    @Override
    public boolean checkExist(String code){
        CtgInfTitle title = repo.findByTitleCode(code);
        return title != null;
    }
}
