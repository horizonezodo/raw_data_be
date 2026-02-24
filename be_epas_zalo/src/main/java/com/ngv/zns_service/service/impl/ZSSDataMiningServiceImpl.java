package com.ngv.zns_service.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ngv.zns_service.util.page.PageUtils;
import org.springframework.data.domain.Page;
import com.ngv.zns_service.service.ZSSDataMiningService;
import com.ngv.zns_service.dto.request.SearchFilterRequest;
import com.ngv.zns_service.repository.ZSSDataMiningRepository;
import com.ngv.zns_service.dto.response.ZSSDataMiningResponse;
import com.ngv.zns_service.dto.request.ZSSDataMiningSearchRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.core.io.ByteArrayResource;

@Service
@RequiredArgsConstructor
public class ZSSDataMiningServiceImpl implements ZSSDataMiningService {
    private final ExcelService excelService;
    private final ZSSDataMiningRepository zssDataMiningRepository;

    @Override
    public Page<ZSSDataMiningResponse> searchAll(SearchFilterRequest filterRequest) {
        Pageable pageable = PageUtils.toPageable(filterRequest.getPageable());
        Page<ZSSDataMiningResponse> zssDataMiningResponses = zssDataMiningRepository.searchAll(filterRequest.getFilter(), pageable);
        return zssDataMiningResponses;
    }


    @Override
    public Page<ZSSDataMiningResponse> findAll(ZSSDataMiningSearchRequest request) {

        List<String> maDvuList = (request.getMaDvuList() != null && request.getMaDvuList().isEmpty()) ? null : request.getMaDvuList();

        Pageable pageable = PageUtils.toPageable(request.getPageable());
        Page<ZSSDataMiningResponse> zssDataMiningResponses = zssDataMiningRepository.search(
                request.getTuNgay(),
                request.getDenNgay(),
                request.getMaDvi(),
                maDvuList,
                request.getTthaiGuizns(),
                pageable
        );
        return zssDataMiningResponses;
    }

    @Override
    public ResponseEntity<ByteArrayResource> exportToExcel(ZSSDataMiningSearchRequest request, String fileName) {
        List<String> maDvuList = (request.getMaDvuList() != null && request.getMaDvuList().isEmpty()) ? null : request.getMaDvuList();
        List<ZSSDataMiningResponse> zssDataMiningResponses = zssDataMiningRepository.searchToExcel(
                request.getTuNgay(),
                request.getDenNgay(),
                request.getMaDvi(),
                maDvuList,
                request.getTthaiGuizns()
        );

        return excelService.exportToExcel(
                zssDataMiningResponses, request.getLabel(), ZSSDataMiningResponse.class, fileName);
    }
}
