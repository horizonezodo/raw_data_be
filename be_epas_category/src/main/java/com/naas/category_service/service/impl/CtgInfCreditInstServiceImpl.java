package com.naas.category_service.service.impl;

import com.naas.category_service.constant.StatusConstants;
import com.naas.category_service.dto.CtgInfCreditInst.CtgInfCreditInstDTO;
import com.naas.category_service.dto.CtgInfCreditInst.ExportCtgInfCreditInstDTO;
import com.naas.category_service.dto.ExportExcelDTO;
import com.naas.category_service.mapper.CtgInfCreditInst.CtgInfCreditInstMapper;
import com.naas.category_service.model.CtgInfCreditInst;
import com.naas.category_service.repository.CtgInfCreditInstRepository;
import com.naas.category_service.service.CtgInfCreditInstService;
import com.ngvgroup.bpm.core.exception.BusinessException;
import com.ngvgroup.bpm.core.exception.ErrorCode;
import com.ngvgroup.bpm.core.service.BaseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CtgInfCreditInstServiceImpl extends BaseService implements CtgInfCreditInstService {
    private final CtgInfCreditInstRepository repo;
    private final CtgInfCreditInstMapper mapper;
    private final ExcelService service;

    @Override
    public Page<CtgInfCreditInstDTO> search(String keyword, Pageable pageable) {
        return repo.search(keyword,pageable);
    }

    @Override
    public CtgInfCreditInstDTO getDetail(String creditInstCode) {
        CtgInfCreditInst ctgInfCreditInst = repo.findByCreditInstCode(creditInstCode);
        return mapper.toDTO(ctgInfCreditInst);
    }

    @Override
    public void updateCreditInst(CtgInfCreditInstDTO dto, String creditInstCode) {
        CtgInfCreditInst ctgInfCreditInst = repo.findByCreditInstCode(creditInstCode);
        if(ctgInfCreditInst != null){
            mapper.updateEntityFromDto(dto,ctgInfCreditInst);
            ctgInfCreditInst.setDescription(dto.getDescription());
            ctgInfCreditInst.setIsDelete(0);
            ctgInfCreditInst.setIsActive(dto.getIsDelete());
            repo.save(ctgInfCreditInst);
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND, creditInstCode);
        }
    }

    @Transactional
    @Modifying
    @Override
    public void deleteCredistInst(String creditInstCode) {
        CtgInfCreditInst ctgInfCreditInst = repo.findByCreditInstCode(creditInstCode);
        if(ctgInfCreditInst != null){
            repo.delete(ctgInfCreditInst);
        }else{
            throw new BusinessException(ErrorCode.NOT_FOUND, creditInstCode);
        }
    }

    @Override
    public void createCreditInst(CtgInfCreditInstDTO dto) {
        CtgInfCreditInst c = repo.findCreditInts(dto.getCreditInstCode(), dto.getCreditInstName(), 1);
        if(c != null){
            mapper.updateEntityFromDto(dto, c);
            c.setIsDelete(0);
            c.setIsActive(dto.getIsDelete());
            c.setDescription(dto.getDescription());
            c.setModifiedDate(c.getTimestampNow());
            repo.save(c);
        }else if (!repo.existsByCreditInstCode(dto.getCreditInstCode())){
            CtgInfCreditInst ctgInfCreditInst = mapper.toE(dto);
            ctgInfCreditInst.setApprovedBy(getCurrentUserName());
            ctgInfCreditInst.setApprovedDate(ctgInfCreditInst.getTimestampNow());
            ctgInfCreditInst.setIsDelete(0);
            ctgInfCreditInst.setIsActive(dto.getIsDelete());
            ctgInfCreditInst.setDescription(dto.getDescription());
            ctgInfCreditInst.setRecordStatus(StatusConstants.APPROVAL);
            ctgInfCreditInst.setModifiedDate(ctgInfCreditInst.getTimestampNow());
            repo.save(ctgInfCreditInst);
        }else{
            throw new BusinessException(ErrorCode.CONFLICT, dto.getCreditInstCode());
        }
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportExcel(ExportExcelDTO dto) {
        List<ExportCtgInfCreditInstDTO> lst = repo.exportData();
        return service.exportToExcel(lst,dto.getLabels(),ExportCtgInfCreditInstDTO.class,dto.getFileName());
    }

    @Override
    public  boolean checkExist(String code){
        CtgInfCreditInst ctgInfCreditInst = repo.findByCreditInstCode(code);
        if(ctgInfCreditInst != null) {
           return true;
        }
        return false;
    }
}
