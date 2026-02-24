package com.ngv.zns_service.service.impl;


import com.ngv.zns_service.constant.ErrorMessages;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.dto.request.ZSSAppRequest;
import com.ngv.zns_service.dto.request.ZSSAppSearchRequest;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.dto.response.ZSSAppResponse;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import com.ngvgroup.bpm.core.persistence.service.storedprocedure.BaseStoredProcedureService;
import com.ngv.zns_service.exception.ValidationException;
import com.ngv.zns_service.mapper.ZSSAppMapper;
import com.ngv.zns_service.model.entity.ZssApp;
import com.ngv.zns_service.model.entity.ZssTKhoanZoa;
import com.ngv.zns_service.repository.ZSSAppRepository;
import com.ngv.zns_service.repository.ZssTKhoanZoaRepository;
import com.ngv.zns_service.service.ZSSAppService;

import com.ngv.zns_service.util.date.DateTimeConverter;
import com.ngv.zns_service.util.page.PageUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ZSSAppServiceImpl extends BaseStoredProcedureService implements ZSSAppService {

    private final ZSSAppRepository zssAppRepository;
    private final ZSSAppMapper zssAppMapper;
    private final ZssTKhoanZoaRepository zssTKhoanZoaRepository;
    
    private final DateTimeConverter dateTimeConverter;
    private final ExcelService excelService;

    @Override
    public Page<ZSSAppResponse> searchAll(SearchFilterRequest filterRequest) {
        String maDvi = filterRequest.getMaDvi();
        String appId = filterRequest.getAppId();
        String tenDvi = filterRequest.getTenDvi();
        Pageable pageable = PageUtils.toPageable(filterRequest.getPageable());
        Page<ZSSAppResponse> page = zssAppRepository.searchAll(maDvi, appId, tenDvi, filterRequest.getFilter(),
                pageable);
        return page;
    }

    @Override
    public ZSSAppResponse getZssApp(String appId) {
        Optional<ZssApp> zssApp = zssAppRepository.findById(appId);
        if (zssApp.isPresent()) {
            return zssAppMapper.toResponse(zssApp.get());
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND, "No app found with id: " + appId);
        }
    }

    @Override
    public void createZssApp(ZSSAppRequest zssAppRequest) throws ValidationException {
        ZssApp zssApp = zssAppMapper.toEntity(zssAppRequest);
        Map<String, String> errors = validateInsertZSSApp(zssApp);
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
        zssApp.setTthaiNvu("DSD");
        zssApp.setNguoiNhap(username);
        zssApp.setNgayNhap(dateTimeNow);
        zssAppRepository.save(zssApp);
    }

    @Override
    public void updateZssApp(ZSSAppRequest zssAppRequest) throws ValidationException {
        ZssApp zssApp = zssAppMapper.toEntity(zssAppRequest);
        Map<String, String> errors = new HashMap<>();
        ZssApp oldZSSApp = zssAppRepository.findById(zssApp.getAppId()).orElse(null);
        if (oldZSSApp == null) {
            errors.put(zssApp.getAppId(), ErrorMessages.NOT_FOUND);
            throw new ValidationException(errors);
        }
        String username = getCurrentUserName();
        String dateTimeNow = dateTimeConverter.dateTimeNow();
        zssApp.setNguoiNhap(oldZSSApp.getNguoiNhap());
        zssApp.setNgayNhap(oldZSSApp.getNgayNhap());
        zssApp.setNguoiSua(username);
        zssApp.setNgaySua(dateTimeNow);
        zssApp.setTthaiNvu(oldZSSApp.getTthaiNvu());
        zssAppRepository.save(zssApp);
    }

    @Override
    public ZssApp getZssAppByOaId(String oaId) {
        ZssTKhoanZoa zoa = zssTKhoanZoaRepository.findById(oaId).orElse(null);
        if (zoa == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "No account zoa found with id: " + oaId);
        }
        ZssApp zssApp = zssAppRepository.findById(zoa.getAppId()).orElse(null);
        if (zssApp == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND, "No app found with id: " + zoa.getAppId());
        }
        return zssApp;
    }

    @Override
    public ZssApp getByWebhookVerificationFile(String whVerificationFile) {
        return zssAppRepository.findByWebhookVerificationFile(whVerificationFile).orElse(null);
    }

    @Override
    public ZssApp getById(String appId) {
        return zssAppRepository.findById(appId).orElse(null);
    }

    @Override
    public void deleteZssApp(String appId) {
        zssAppRepository.deleteById(appId);
    }

    private Map<String, String> validateInsertZSSApp(ZssApp zssApp) {
        Map<String, String> errors = new HashMap<>();
        if (zssAppRepository.findById(zssApp.getAppId()).isPresent()) {
            errors.put(zssApp.getAppName(), ErrorMessages.ALREADY_EXISTS);
        }
        return errors;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZSSAppSearchRequest request, String fileName) {
        List<ZSSAppResponse> zssAppResponses = zssAppRepository.searchToExcel(null, null, null);
        return excelService.exportToExcel(zssAppResponses, request.getLabel(), ZSSAppResponse.class,
                fileName);
    }
}
