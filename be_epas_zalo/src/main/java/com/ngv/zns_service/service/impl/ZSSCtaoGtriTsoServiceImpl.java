package com.ngv.zns_service.service.impl;


import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.excel.ZSSCtaoGtriTsoExcel;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoRequest;
import com.ngv.zns_service.dto.request.ZSSCtaoGtriTsoSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSCtaoGtriTsoResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.mapper.ZSSCtaoGtriTsoMapper;
import com.ngv.zns_service.model.entity.ZSSCtaoGtriTso;
import com.ngv.zns_service.repository.ZSSCtaoGtriTsoRepository;
import com.ngv.zns_service.service.ZSSCtaoGtriTsoService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngv.zns_service.util.page.PageUtils;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZSSCtaoGtriTsoServiceImpl extends BaseStoredProcedureService implements ZSSCtaoGtriTsoService {
    private final ZSSCtaoGtriTsoRepository zssCtaoGtriTsoRepository;
    private final ExcelService excelService;
    private final ZSSCtaoGtriTsoMapper ctaoGtriTsoMapper;
    
    private final DateTimeConverter dateTimeConverter;

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZSSCtaoGtriTsoSearchRequest request, String fileName) {
        List<ZSSCtaoGtriTsoResponse> zssCtaoGtriTsos = zssCtaoGtriTsoRepository.searchToExportExcel();
        List<ZSSCtaoGtriTsoExcel> excelData = zssCtaoGtriTsos.stream()
                .map(response -> new ZSSCtaoGtriTsoExcel(
                        response.getMaCtaoGtriTso(),
                        response.getTenCtaoGtriTso()
                ))
                .toList();
        return excelService.exportToExcel(
                excelData, request.getLabel(), ZSSCtaoGtriTsoExcel.class, fileName);


    }

    @Override
    public Page<ZSSCtaoGtriTsoResponse> searchAll(SearchFilterRequest filterRequest) {
        Pageable pageable = PageUtils.toPageable(filterRequest.getPageable());
        Page<ZSSCtaoGtriTsoResponse> ctaoGtriTsoResponses = zssCtaoGtriTsoRepository.searchAll(filterRequest.getFilter(), pageable);
        return ctaoGtriTsoResponses;
    }

    @Override
    public void create(ZSSCtaoGtriTsoRequest rq) throws ValidationException {
        ZSSCtaoGtriTso zssCtaoGtriTso = ctaoGtriTsoMapper.toEntity(rq);
        zssCtaoGtriTso.setTthaiNvu("DSD");
        boolean checkMaCtaoTsoExits = zssCtaoGtriTsoRepository.existsByMaCtaoGtriTso(zssCtaoGtriTso.getMaCtaoGtriTso());
        if (checkMaCtaoTsoExits) {
            throw new ValidationException("maCtaoGtriTso: " + zssCtaoGtriTso.getMaCtaoGtriTso(), ErrorMessages.ALREADY_EXISTS);
        }
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
        zssCtaoGtriTso.setNguoiNhap(username);
        zssCtaoGtriTso.setNgayNhap(dateTimeNow);
        zssCtaoGtriTsoRepository.save(zssCtaoGtriTso);


    }

    @Override
    public void update(String maCtaoGtriTso, ZSSCtaoGtriTsoRequest rq) throws ValidationException {
        Optional<ZSSCtaoGtriTso> optionalZSSCtaoGtriTso = zssCtaoGtriTsoRepository.findById(maCtaoGtriTso);
        Integer count = zssCtaoGtriTsoRepository.checkMaCtaoGtriTsoUpdateExist(rq.getMaCtaoGtriTso(), maCtaoGtriTso);
        if (count > 0) {
            throw new ValidationException("maCtaoGtriTso: " + rq.getMaCtaoGtriTso(), ErrorMessages.ALREADY_EXISTS);
        }
        if (optionalZSSCtaoGtriTso.isPresent()) {
            String username = getCurrentUserName();
            String dateTimeNow = dateTimeConverter.dateTimeNow();
            ZSSCtaoGtriTso ctaoGtriTso = optionalZSSCtaoGtriTso.get();
            ctaoGtriTso.setMaCtaoGtriTso(rq.getMaCtaoGtriTso());
            ctaoGtriTso.setTenCtaoGtriTso(rq.getTenCtaoGtriTso());
            ctaoGtriTso.setCthucCtaoGtriTso(rq.getCthucCtaoGtriTso());
            ctaoGtriTso.setLoaiGiaTri(rq.getLoaiGiaTri());
            ctaoGtriTso.setMdichSdung(rq.getMdichSdung());
            ctaoGtriTso.setNguoiSua(username);
            ctaoGtriTso.setNgaySua(dateTimeNow);
            zssCtaoGtriTsoRepository.save(ctaoGtriTso);
        } else {
            throw new ValidationException("maCtaoGtriTso: " + maCtaoGtriTso, ErrorMessages.NOT_FOUND);
        }

    }

    @Override
    public void delete(String maCtaoGtriTso) throws ValidationException {
        Optional<ZSSCtaoGtriTso> optionalZSSCtaoGtriTso = zssCtaoGtriTsoRepository.findById(maCtaoGtriTso);
        if (optionalZSSCtaoGtriTso.isEmpty()) {
            throw new ValidationException("maCtaoGtriTso: " + maCtaoGtriTso, ErrorMessages.NOT_FOUND);
        }
        zssCtaoGtriTsoRepository.deleteById(maCtaoGtriTso);
    }
}
