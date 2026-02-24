package com.ngv.zns_service.dto.response.goiDv;
import lombok.*;

import java.util.List;

@Data
public class ZSSGoiDvuDetailDto {
    private GoiDvuDetailTTinChungDto tTinChungDto;
    private List<GoiDvuDetailMappingDvuDto> mappingDvuDtos;

    public ZSSGoiDvuDetailDto() {
    }

    public ZSSGoiDvuDetailDto(GoiDvuDetailTTinChungDto tTinChungDto, List<GoiDvuDetailMappingDvuDto> mappingDvuDtos) {
        this.tTinChungDto = tTinChungDto;
        this.mappingDvuDtos = mappingDvuDtos;
    }

    public GoiDvuDetailTTinChungDto gettTinChungDto() {
        return tTinChungDto;
    }

    public void settTinChungDto(GoiDvuDetailTTinChungDto tTinChungDto) {
        this.tTinChungDto = tTinChungDto;
    }

    public List<GoiDvuDetailMappingDvuDto> getMappingDvuDtos() {
        return mappingDvuDtos;
    }

    public void setMappingDvuDtos(List<GoiDvuDetailMappingDvuDto> mappingDvuDtos) {
        this.mappingDvuDtos = mappingDvuDtos;
    }
}
