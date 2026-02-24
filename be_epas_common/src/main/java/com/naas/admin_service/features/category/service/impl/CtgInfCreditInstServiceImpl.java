package com.naas.admin_service.features.category.service.impl;

import com.naas.admin_service.core.contants.CommonErrorCode;
import com.naas.admin_service.core.contants.Constant;
import com.naas.admin_service.features.category.dto.CtgInfCreditInstDTO;
import com.naas.admin_service.features.category.dto.ExportCtgInfCreditInstDTO;
import com.naas.admin_service.features.category.mapper.CtgInfCreditInstMapper;
import com.naas.admin_service.features.category.model.CtgInfCreditInst;
import com.naas.admin_service.features.category.repository.CtgInfCreditInstRepository;
import com.naas.admin_service.features.category.service.CtgInfCreditInstService;
import com.naas.admin_service.features.category.dto.ExportExcelDTO;
import com.naas.admin_service.core.excel.service.ExcelService;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;


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
public class CtgInfCreditInstServiceImpl extends BaseStoredProcedureService implements CtgInfCreditInstService {
    private final CtgInfCreditInstRepository repo;
    private final CtgInfCreditInstMapper mapper;
    private final ExcelService service;

    protected CtgInfCreditInstServiceImpl(CtgInfCreditInstRepository repo, CtgInfCreditInstMapper mapper, ExcelService service) {
        super();
        this.repo = repo;
        this.mapper = mapper;
        this.service = service;
    }

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
            repo.save(c);
        }else if (!repo.existsByCreditInstCode(dto.getCreditInstCode())){
            CtgInfCreditInst ctgInfCreditInst = mapper.toE(dto);
            ctgInfCreditInst.setApprovedBy(getCurrentUserName());
            ctgInfCreditInst.setIsDelete(0);
            ctgInfCreditInst.setIsActive(dto.getIsDelete());
            ctgInfCreditInst.setDescription(dto.getDescription());
            ctgInfCreditInst.setRecordStatus(Constant.APPROVAL);
            repo.save(ctgInfCreditInst);
        }else{
            throw new BusinessException(CommonErrorCode.EXIST_CREDIT_INST_ID, dto.getCreditInstCode());
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
        return ctgInfCreditInst != null;
    }
}
