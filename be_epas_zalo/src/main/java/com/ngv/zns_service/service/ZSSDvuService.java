package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.request.ZSSDvuRequest;
import com.ngv.zns_service.dto.response.ZSSDvuResponse;
import com.ngv.zns_service.dto.response.dvu.*;
import com.ngv.zns_service.exception.ValidationException;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ZSSDvuService {
    Page<ZSSDvuResponse> getList(ZSSDvuRequest request);

    void deleteIds(List<String> maDvu) throws ValidationException;

    byte[] exportExcel(ZSSDvuRequest request) throws IOException;

    List<ZSSLoaiDvuResponse> listLoaiDvu(String maDmCtiet);

    ChiTietDvuDto getDetail(String maDichVu);

    ChiTietDvuDto create(ChiTietDvuDto dto) throws ValidationException;

    ChiTietDvuDto update(ChiTietDvuDto dto) throws ValidationException;

    List<CtDvMapGdvDto> listMapGdvDtos();

    List<CtDvMapGdDto> listMapGdDtos();

    List<ZSSLoaiDvuDto> listCommon(String mdm, String mdmct);

    CtDvMapTempDto saveTemp(CtDvMapTempDto dto);

    Map<String, List<Map<String, String>>> getGroupedDvu();
}
