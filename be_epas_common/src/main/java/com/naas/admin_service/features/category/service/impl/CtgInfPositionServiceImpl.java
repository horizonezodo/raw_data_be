package com.naas.admin_service.features.category.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.category.dto.CtgInfPositionDTO;
import com.naas.admin_service.features.category.dto.ExportCtgInfPositionDTO;
import com.naas.admin_service.features.category.dto.ExportExcelDTO;
import com.naas.admin_service.features.category.mapper.CtgInfPositionMapper;
import com.naas.admin_service.features.category.model.CtgInfPosition;
import com.naas.admin_service.features.category.repository.CtgInfPositionRepository;
import com.naas.admin_service.features.category.service.CtgInfPositionService;
import com.naas.admin_service.features.category.model.CtgInfTitle;
import com.naas.admin_service.features.category.repository.CtgInfTitleRepository;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import jakarta.transaction.Transactional;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CtgInfPositionServiceImpl extends BaseStoredProcedureService implements CtgInfPositionService {

    private final CtgInfPositionRepository repo;
    private final CtgInfPositionMapper mapper;
    private final ExcelService service;
    private final CtgInfTitleRepository titleRepo;

    protected CtgInfPositionServiceImpl(CtgInfPositionRepository repo, CtgInfPositionMapper mapper,
            ExcelService service, CtgInfTitleRepository titleRepo) {
        super();
        this.repo = repo;
        this.mapper = mapper;
        this.service = service;
        this.titleRepo = titleRepo;
    }

    @Override
    public void createPosition(CtgInfPositionDTO dto) {
        CtgInfPosition position = repo.checkPosition(dto.getPositionCode(), dto.getPositionName(), 1);
        if (position != null) {
            mapper.updateEntityFromDto(dto, position);
            position.setIsDelete(0);
            position.setIsActive(dto.getIsDelete());
            position.setDescription(dto.getDescription());
            repo.save(position);
        } else if (!repo.existsByPositionCode(dto.getPositionCode())) {
            CtgInfPosition hrmInfPosition = mapper.toE(dto);
            hrmInfPosition.setIsDelete(0);
            hrmInfPosition.setDescription(dto.getDescription());
            hrmInfPosition.setOrgCode("%");
            hrmInfPosition.setIsActive(dto.getIsDelete());
            hrmInfPosition.setRecordStatus(Constant.APPROVAL);
            repo.save(hrmInfPosition);
        } else {
            throw new BusinessException(CommonErrorCode.EXIST_POSITION_ID, dto.getPositionCode());
        }
    }

    @Override
    public void updatePosition(String positionCode, CtgInfPositionDTO dto) {
        CtgInfPosition position = repo.findByPositionCode(positionCode);
        if (position != null) {
            mapper.updateEntityFromDto(dto, position);
            position.setDescription(dto.getDescription());
            position.setIsDelete(0);
            position.setIsActive(dto.getIsDelete());
            repo.save(position);
        } else {
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
            throw new BusinessException(CommonErrorCode.POSITION_IS_USE, positionCode);
        }
        repo.delete(position);
    }

    @Override
    public Page<CtgInfPositionDTO> search(String keyword, Pageable pageable) {
        return repo.search(keyword, pageable);
    }

    @Override
    public CtgInfPositionDTO getDetail(String positionCode) {
        CtgInfPosition position = repo.findByPositionCode(positionCode);
        if (position != null) {
            return mapper.toDTO(position);
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND, positionCode);
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto) {
        List<ExportCtgInfPositionDTO> lst = repo.exportData();
        return service.exportToExcel(lst, dto.getLabels(), ExportCtgInfPositionDTO.class, dto.getFileName());
    }

    @Override
    public List<ExportCtgInfPositionDTO> listPosition() {
        return repo.exportData();
    }

    @Override
    public boolean checkExist(String code) {
        CtgInfPosition position = repo.findByPositionCode(code);
        return position != null;
    }
}
