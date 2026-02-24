package com.ngv.zns_service.service.impl;


import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.excel.ZZSDviThueBaoExcel;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSDviThueBaoRequest;
import com.ngv.zns_service.dto.request.ZSSDviThueBaoSearchRequest;
import com.ngv.zns_service.dto.response.DCDviDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ngv.zns_service.dto.response.ZSSDviThueBaoResponse;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.mapper.ZSSDviThueBaoMapper;
import com.ngv.zns_service.model.entity.ZSSDmucCtiet;
import com.ngv.zns_service.model.entity.ZSSDviThueBao;
import com.ngv.zns_service.model.entity.ZSSGoiDvu;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import com.ngv.zns_service.repository.*;
import com.ngv.zns_service.service.ZSSDviThueBaoService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngv.zns_service.util.page.PageUtils;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZSSDviThueBaoServiceImpl extends BaseStoredProcedureService implements ZSSDviThueBaoService {
    private final ZSSDviThueBaoRepository zssDviThueBaoRepository;
    private final ZSSGoiDvuRepository zssGoiDvuRepository;
    private final DCDviRepository dcDviRepository;
    private final ZSSDmucCtietRepository zssDmucCtietRepository;
    private final ZssTKhoanZoaRepository zssTKhoanZoaRepository;
    private final ExcelService excelService;
    private final ZSSDviThueBaoMapper zssDviThueBaoMapper;
    
    private final DateTimeConverter dateTimeConverter;

    @Override
    public Page<ZSSDviThueBaoResponse> findAll(ZSSDviThueBaoSearchRequest request) {
        Pageable pageable = PageUtils.toPageable(request.getPageable());
        Page<ZSSDviThueBaoResponse> zssDviThueBaoList = zssDviThueBaoRepository.search(
                request.getMaGoiDvu(),
                request.getTthaiNvu(),
                request.getTuNgayDky(),
                request.getDenNgayDky(),
                request.getTuNgayHetHluc(),
                request.getNgayHetHluc(),
                pageable);
        return zssDviThueBaoList;
    }

    @Override
    public Map<String, String> listTenGoiDvu() {
        List<ZSSGoiDvu> zssGoiDvus = zssGoiDvuRepository.findAll();
        return zssGoiDvus.stream().collect(Collectors.toMap(ZSSGoiDvu::getMaGoiDvu, ZSSGoiDvu::getTenGoiDvu));
    }

    @Override
    public Map<String, String> listThai() {
        List<ZSSDmucCtiet> zssDmucCtiets = zssDmucCtietRepository.listTthai();
        return zssDmucCtiets.stream()
                .collect(Collectors.toMap(ZSSDmucCtiet::getMaDmucCtiet, ZSSDmucCtiet::getTenDmucCtiet));
    }

    @Override
    public Map<String, String> listTenDvi() {
        List<DCDviDto> dcDvis = dcDviRepository.listTenDvi();
        return dcDvis.stream()
                .distinct()
                .collect(Collectors.toMap(
                        DCDviDto::getMaDvi,
                        DCDviDto::getTenDvi));
    }

    @Override
    public Map<String, String> listZOAID(String maDvi) {
        List<ZssTKhoanZoa> zssTKhoanZoas = zssTKhoanZoaRepository.findAllByMaDvi(maDvi);
        return zssTKhoanZoas.stream().collect(Collectors.toMap(ZssTKhoanZoa::getMaZoa, ZssTKhoanZoa::getTenZoa));
    }

    @Override
    public Map<String, String> listTThaiNvu() {
        List<ZSSDviThueBao> zssDviThueBaoList = zssDviThueBaoRepository.findAll();
        return zssDviThueBaoList.stream()
                .collect(Collectors.toMap(ZSSDviThueBao::getMaThueBao, ZSSDviThueBao::getTthaiNvu));
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZSSDviThueBaoSearchRequest request, String fileName) {
        List<ZSSDviThueBaoResponse> zssDviThueBaoResponses = zssDviThueBaoRepository.searchToExportExcel(
                request.getMaGoiDvu(),
                request.getTthaiNvu(),
                request.getTuNgayDky(),
                request.getDenNgayDky(),
                request.getTuNgayHetHluc(),
                request.getNgayHetHluc());
        List<ZZSDviThueBaoExcel> excelData = zssDviThueBaoResponses.stream()
                .map(response -> new ZZSDviThueBaoExcel(
                        response.getMaDvi(),
                        response.getZoaId(),
                        response.getTenDvi(),
                        response.getMaGoiDvu(),
                        response.getTenGoiDvu(),
                        response.getNgayDky(),
                        response.getNgayHetHluc(),
                        response.getTthaiNvu()

                ))
                .collect(Collectors.toList());
        return excelService.exportToExcel(excelData, request.getLabel(), ZZSDviThueBaoExcel.class, fileName);

    }

    @Override
    public Page<ZSSDviThueBaoResponse> searchAll(SearchFilterRequest request) {
        Pageable pageable = PageUtils.toPageable(request.getPageable());
        Page<ZSSDviThueBaoResponse> zssDviThueBaoResponses = zssDviThueBaoRepository.searchAll(request.getFilter(),
                pageable);
        return zssDviThueBaoResponses;
    }

    @Override
    public void create(ZSSDviThueBaoRequest rq) throws ValidationException {
        ZSSDviThueBao zssDviThueBao = zssDviThueBaoMapper.toEntity(rq);
        String maThueBaoUUID = UUID.randomUUID().toString().replace("-", "");
        zssDviThueBao.setMaThueBao(maThueBaoUUID);
        Map<String, String> errors = validateInsertDviThueBao(zssDviThueBao);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
        zssDviThueBao.setNguoiNhap(username);
        zssDviThueBao.setNgayNhap(dateTimeNow);
        zssDviThueBaoRepository.save(zssDviThueBao);

    }

    @Override
    public void update(String maThueBao, ZSSDviThueBaoRequest rq) throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        Optional<ZSSDviThueBao> zssDviThueBaoOptional = zssDviThueBaoRepository.findById(maThueBao);
        if (zssDviThueBaoOptional.isEmpty()) {
            errors.put(maThueBao, ErrorMessages.NOT_FOUND);
        } else {
            ZSSDviThueBao existingZSSDviThueBao = zssDviThueBaoOptional.get();
            ZSSDviThueBao zssDviThueBao = zssDviThueBaoMapper.toEntity(rq);
            errors = validateUpdateDviThueBao(zssDviThueBao, existingZSSDviThueBao);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
        ZSSDviThueBao zssDviThueBao = zssDviThueBaoOptional.get();
        zssDviThueBao
                .setMaDvi(rq.getMaDvi())
                .setMaGoiDvu(rq.getMaGoiDvu())
                .setNgayDky(rq.getNgayDky())
                .setNgayHluc(rq.getNgayHluc())
                .setTgian(rq.getTgian())
                .setDviTgian(rq.getDviTgian())
                .setNgayHetHluc(rq.getNgayHetHluc())
                .setTthaiNvu(rq.getTthaiNvu())
                .setNguoiSua(username)
                .setNgaySua(dateTimeNow);

        zssDviThueBaoRepository.save(zssDviThueBao);
    }

    @Override
    public void delete(String maThueBao) throws ValidationException {
        Optional<ZSSDviThueBao> zssDviThueBaoOptional = zssDviThueBaoRepository.findById(maThueBao);
        if (zssDviThueBaoOptional.isEmpty()) {
            throw new ValidationException(maThueBao, ErrorMessages.NOT_FOUND);
        }
        zssDviThueBaoRepository.deleteById(maThueBao);
    }

    @Override
    public void stop(String maThueBao) throws ValidationException {
        Map<String, String> errors = new HashMap<>();
        Optional<ZSSDviThueBao> zssDviThueBaoOptional = zssDviThueBaoRepository.findById(maThueBao);
        if (zssDviThueBaoOptional.isEmpty()) {
            errors.put(maThueBao, ErrorMessages.NOT_FOUND);
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        ZSSDviThueBao zssDviThueBao = zssDviThueBaoOptional.get();
        zssDviThueBao.setTthaiNvu("NSD");
        zssDviThueBaoRepository.save(zssDviThueBao);
    }

    private Map<String, String> validateInsertDviThueBao(ZSSDviThueBao zssDviThueBao) {
        Map<String, String> errors = new HashMap<>();
        if (zssDviThueBaoRepository.existsByZoaId(zssDviThueBao.getZoaId())) {
            errors.put(zssDviThueBao.getMaThueBao(), ErrorMessages.ALREADY_EXISTS);
        }
        return errors;
    }

    private Map<String, String> validateUpdateDviThueBao(ZSSDviThueBao zssDviThueBao,
            ZSSDviThueBao existingZssDviThueBao) {
        Map<String, String> errors = new HashMap<>();
        if (!existingZssDviThueBao.getMaDvi().equals(zssDviThueBao.getMaDvi()) &&
                zssDviThueBaoRepository.existsByMaDvi(zssDviThueBao.getMaDvi())) {
            errors.put(zssDviThueBao.getMaDvi(), ErrorMessages.ALREADY_EXISTS);
        }
        return errors;
    }

}
