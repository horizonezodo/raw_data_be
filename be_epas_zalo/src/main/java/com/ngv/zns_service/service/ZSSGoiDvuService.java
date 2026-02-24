package com.ngv.zns_service.service;

import com.ngv.zns_service.dto.response.goiDv.ZSSGoiDvuDetailDto;
import com.ngv.zns_service.dto.response.goiDv.ZSSGoiDvuDto;
import com.ngv.zns_service.dto.response.goiDv.ZSSGoiDvuDtos;
import com.ngv.zns_service.dto.response.goiDv.ZSSTthaiHdongDto;
import com.ngv.zns_service.exception.ValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ZSSGoiDvuService {
    ZSSGoiDvuDtos syncData(String maDvi);

    Page<ZSSGoiDvuDto> listGoiDvu(Pageable pageable);

    void deleteIds(List<String> maGoiDvu) throws ValidationException;

    ZSSGoiDvuDetailDto create(ZSSGoiDvuDetailDto dto) throws ValidationException;

    ZSSGoiDvuDetailDto update(ZSSGoiDvuDetailDto dto) throws ValidationException;

    ZSSGoiDvuDetailDto getDetail(String mgdv);

    byte[] exportExcel() throws IOException;

    List<ZSSTthaiHdongDto> listTthaiHd(String mdm);
}
