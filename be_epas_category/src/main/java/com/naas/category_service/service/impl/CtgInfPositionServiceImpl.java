package com.naas.category_service.service.impl;

import com.naas.category_service.constant.StatusConstants;
import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.dto.CtgInfPosition.ExportCtgInfPositionDTO;
import com.naas.category_service.dto.CtgInfPosition.CtgInfPositionDTO;
import com.naas.category_service.exception.CategoryErrorCode;
import com.naas.category_service.mapper.CtgInfPosition.CtgInfPositionMapper;
import com.naas.category_service.model.CtgInfPosition;
import com.naas.category_service.model.CtgInfTitle;
import com.naas.category_service.repository.CtgInfPositionRepository;
import com.naas.category_service.repository.CtgInfTitleRepository;
import com.naas.category_service.service.CtgInfPositionService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import com.ngvgroup.bpm.core.service.BaseService;
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
public class CtgInfPositionServiceImpl extends BaseService implements CtgInfPositionService {
    private final CtgInfPositionRepository repo;
    private final CtgInfPositionMapper mapper;
    private final ExcelService service;
    private final CtgInfTitleRepository titleRepo;

    @Override
    public void createPosition(CtgInfPositionDTO dto) {
        CtgInfPosition position = repo.checkPosition(dto.getPositionCode(), dto.getPositionName(),1);
        if(position != null){
            mapper.updateEntityFromDto(dto,position);
            position.setIsDelete(0);
            position.setIsActive(dto.getIsDelete());
            position.setDescription(dto.getDescription());
            position.setModifiedDate(position.getTimestampNow());
            repo.save(position);
        }else if(!repo.existsByPositionCode(dto.getPositionCode())){
            CtgInfPosition hrmInfPosition = mapper.toE(dto);
            hrmInfPosition.setApprovedBy(getCurrentUserName());
            hrmInfPosition.setApprovedDate(hrmInfPosition.getTimestampNow());
            hrmInfPosition.setIsDelete(0);
            hrmInfPosition.setDescription(dto.getDescription());
            hrmInfPosition.setOrgCode("%");
            hrmInfPosition.setIsActive(dto.getIsDelete());
            hrmInfPosition.setRecordStatus(StatusConstants.APPROVAL);
            hrmInfPosition.setModifiedDate(hrmInfPosition.getTimestampNow());
            repo.save(hrmInfPosition);
        }else{
            throw new BusinessException(ErrorCode.CONFLICT, dto.getPositionCode() + "-" + dto.getPositionName());
        }
    }

    @Override
    public void updatePosition(String positionCode, CtgInfPositionDTO dto) {
        CtgInfPosition position = repo.findByPositionCode(positionCode);
        if(position != null){
            mapper.updateEntityFromDto(dto,position);
            position.setDescription(dto.getDescription());
            position.setIsDelete(0);
            position.setIsActive(dto.getIsDelete());
            repo.save(position);
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND, positionCode);
        }
    }

    @Transactional
    @Modifying
    @Override
    public void deletePosition(String positionCode) {
        CtgInfPosition position = repo.findByPositionCode(positionCode);
        if (position == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, positionCode);
        }

        List<CtgInfTitle> titles = titleRepo.findAllByPositionCode(positionCode);
        if (!titles.isEmpty()) {
            throw new BusinessException(CategoryErrorCode.POSITION_IS_USE, positionCode);
        }
        repo.delete(position);
    }

    @Override
    public Page<CtgInfPositionDTO> search(String keyword, Pageable pageable) {
        return repo.search(keyword,pageable);
    }

    @Override
    public CtgInfPositionDTO getDetail(String positionCode) {
        CtgInfPosition position = repo.findByPositionCode(positionCode);
        if(position != null){
           return mapper.toDTO(position);
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND, positionCode);
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto) {
        List<ExportCtgInfPositionDTO> lst = repo.exportData();
        return service.exportToExcel(lst,dto.getLabels(), ExportCtgInfPositionDTO.class, dto.getFileName());
    }

    @Override
    public List<ExportCtgInfPositionDTO> listPosition() {
        return repo.exportData();
    }

    @Override
    public  boolean checkExist(String code){
        CtgInfPosition position = repo.findByPositionCode(code);
        if(position != null) return true;
        return false;
    }
}
