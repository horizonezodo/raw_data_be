package com.naas.category_service.service.impl;

import com.naas.category_service.constant.StatusConstants;
import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.dto.CtgInfTitle.ExportCtgInfTitleDTO;
import com.naas.category_service.dto.CtgInfTitle.CtgInfTitleDTO;
import com.naas.category_service.mapper.CtgInfTitle.CtgInfTitleMapper;
import com.naas.category_service.model.BaseEntity;
import com.naas.category_service.model.CtgInfTitle;
import com.naas.category_service.repository.CtgInfTitleRepository;
import com.naas.category_service.service.CtgInfTitleService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
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
            title.setModifiedDate(title.getTimestampNow());
            repo.save(title);
        }else if(!repo.existsByTitleCode(dto.getTitleCode())){
            CtgInfTitle hrmInfTitle = mapper.toE(dto);
            hrmInfTitle.setApprovedBy(getCurrentUsername());
            hrmInfTitle.setApprovedDate(hrmInfTitle.getTimestampNow());
            hrmInfTitle.setIsDelete(0);
            hrmInfTitle.setIsActive(dto.getIsDelete());
            hrmInfTitle.setRecordStatus(StatusConstants.APPROVAL);
            hrmInfTitle.setDescription(dto.getDescription());
            hrmInfTitle.setModifiedDate(hrmInfTitle.getTimestampNow());
            repo.save(hrmInfTitle);
        }
        else{
            throw new BusinessException(ErrorCode.CONFLICT, dto.getTitleCode());
        }
    }

    @Override
    public void updateTitle(String TitleCode, CtgInfTitleDTO dto) {
        CtgInfTitle title = repo.findByTitleCode(TitleCode);
        if(title !=null){
            mapper.updateEntityFromDto(dto, title);
            title.setIsDelete(0);
            title.setIsActive(dto.getIsDelete());
            title.setDescription(dto.getDescription());
            repo.save(title);
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, TitleCode);
        }
    }

    @Transactional
    @Modifying
    @Override
    public void deleteTitle(String TitleCode) {
        CtgInfTitle title = repo.findByTitleCode(TitleCode);
        if(title !=null){
            repo.delete(title);
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, TitleCode);
        }
    }

    @Override
    public Page<CtgInfTitleDTO> search(String keyword, Pageable pageable) {
        return repo.search(keyword,pageable);
    }

    @Override
    public CtgInfTitleDTO getDetail(String TitleCode) {
        CtgInfTitle title = repo.findByTitleCode(TitleCode);
        if(title !=null){
            return mapper.toDTO(title);
        }
        else{
            throw new BusinessException(ErrorCode.NOT_FOUND, TitleCode);
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
        if(title !=null) return true;
        return false;
    }
}
