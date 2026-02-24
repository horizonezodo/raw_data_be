package com.ngv.zns_service.service.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZssTKhoanZoaSearchRequest;
import com.ngv.zns_service.dto.response.DCDviDto;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZssTKhoanZoaResponse;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import com.ngv.zns_service.repository.ZSSAppRepository;
import com.ngv.zns_service.repository.ZssTKhoanZoaRepository;
import com.ngv.zns_service.repository.DCDviRepository;
import com.ngv.zns_service.service.OAService;
import com.ngv.zns_service.service.ZNSAuthService;
import com.ngv.zns_service.service.ZssTKhoanZoaService;
import com.ngv.zns_service.util.page.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
public class ZssTKhoanZOAServiceImpl implements ZssTKhoanZoaService {

    private final ZssTKhoanZoaRepository zssTKhoanZoaRepository;
    private final ZSSAppRepository zSSAppRepository;
    private final DCDviRepository dcDviRepository;
    private final ZNSAuthService znsAuthService;
    private final OAService oAService;
    private final ExcelService excelService;

    @Override
    public ZssTKhoanZoa setToken(String maZoa, String authorizationCode, String state) {
        ZssTKhoanZoa zssTKhoanZoa = znsAuthService.requestAccessToken(maZoa, authorizationCode, state);
        ObjectNode oaInfo = oAService.getOaInfo(zssTKhoanZoa.getAccessToken());
        if (oaInfo.get("error").asInt() == 0) {
            zssTKhoanZoa.setTenZoa(oaInfo.get("data").get("name").asText());
            addZoa(zssTKhoanZoa);
        } else {
            log.info(oaInfo.toString());
        }
        return zssTKhoanZoa;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ZssTKhoanZoa getTokenInfo(String maZoa) {
        ZssTKhoanZoa zssTKhoanZoa = zssTKhoanZoaRepository.findZssTKhoanZoaByMaZoa(maZoa);
        ZssApp zssApp = zSSAppRepository.findById(zssTKhoanZoa.getAppId()).orElse(null);
        if (zssTKhoanZoa.getNgayDhanTk() < System.currentTimeMillis()) {
            ZssTKhoanZoa zoaNew = znsAuthService.refreshAccessToken(maZoa, zssTKhoanZoa.getRefreshToken(), zssApp);
            zssTKhoanZoa.setAccessToken(zoaNew.getAccessToken());
            zssTKhoanZoa.setRefreshToken(zoaNew.getRefreshToken());
            zssTKhoanZoa.setNgayDhanTk(zoaNew.getNgayDhanTk());
            updateZoa(zssTKhoanZoa);
        }
        return zssTKhoanZoa;
    }

    @Override
    public List<ObjectNode> getAll(Integer page, Integer size) {
        List<ZssTKhoanZoa> tKhoanZoas = zssTKhoanZoaRepository.findAll();
        return List.of();
    }

    @Override
    public ZssTKhoanZoa getZoa(String maZoa) {
        ZssTKhoanZoa zoa = zssTKhoanZoaRepository.findZssTKhoanZoaByMaZoa(maZoa);
        if (zoa == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "ZssTKhoanZoa not found");
        }
        return zoa;
    }

    @Override
    public ZssTKhoanZoa addZoa(ZssTKhoanZoa zssTKhoanZoa) {
        return zssTKhoanZoaRepository.save(zssTKhoanZoa);
    }

    @Override
    public ZssTKhoanZoa updateZoa(ZssTKhoanZoa zssTKhoanZoa) {
        if (zssTKhoanZoaRepository.findZssTKhoanZoaByMaZoa(zssTKhoanZoa.getMaZoa()) == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "ZssTKhoanZoa not found");
        }
        return zssTKhoanZoaRepository.save(zssTKhoanZoa);
    }

    @Override
    public ZssTKhoanZoa deleteZoa(String maZoa) {
        ZssTKhoanZoa zoa = zssTKhoanZoaRepository.findZssTKhoanZoaByMaZoa(maZoa);
        if (zoa == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "ZssTKhoanZoa not found");
        }
        zssTKhoanZoaRepository.delete(zoa);
        return zoa;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZssTKhoanZoaSearchRequest request, String fileName) {
        List<ZssTKhoanZoaResponse> zssTKhoanZoaResponses = zssTKhoanZoaRepository.searchToExcel();
        return excelService.exportToExcel(zssTKhoanZoaResponses, request.getLabel(), ZssTKhoanZoaResponse.class,
                fileName);
    }

    @Override
    public Page<ZssTKhoanZoaResponse> searchAll(SearchFilterRequest filterRequest) {
        Pageable pageable = PageUtils.toPageable(filterRequest.getPageable());
        Page<ZssTKhoanZoaResponse> page = zssTKhoanZoaRepository.searchAll(filterRequest.getFilter(), pageable);
        return page;
    }

    @Override
    public Map<String, String> listTenDviAll() {
        List<DCDviDto> dcDvis = dcDviRepository.getAllDCDviDto();
        return dcDvis.stream().collect(Collectors.toMap(
                DCDviDto::getMaDvi,
                DCDviDto::getTenDvi,
                (existing, replacement) -> existing));
    }
}
