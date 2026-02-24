package com.ngv.zns_service.dto.response.dvu;

import lombok.Data;

import java.util.List;

@Data
public class ChiTietDvuDto {
    private CtDvTtchungDto ttchungDto;
    private List<CtDvMapGdvDto> gdvDtos;
    private List<CtDvMapGdDto> gdDtos;
    private List<CtDvMapTempDto> tempDtos;

    public ChiTietDvuDto() {
    }

    public ChiTietDvuDto(CtDvTtchungDto ttchungDto, List<CtDvMapGdvDto> gdvDtos, List<CtDvMapGdDto> gdDtos,
                         List<CtDvMapTempDto> tempDtos) {
        this.ttchungDto = ttchungDto;
        this.gdvDtos = gdvDtos;
        this.gdDtos = gdDtos;
        this.tempDtos = tempDtos;
    }

    public CtDvTtchungDto getTtchungDto() {
        return ttchungDto;
    }

    public void setTtchungDto(CtDvTtchungDto ttchungDto) {
        this.ttchungDto = ttchungDto;
    }

    public List<CtDvMapGdvDto> getGdvDtos() {
        return gdvDtos;
    }

    public void setGdvDtos(List<CtDvMapGdvDto> gdvDtos) {
        this.gdvDtos = gdvDtos;
    }

    public List<CtDvMapGdDto> getGdDtos() {
        return gdDtos;
    }

    public void setGdDtos(List<CtDvMapGdDto> gdDtos) {
        this.gdDtos = gdDtos;
    }

    public List<CtDvMapTempDto> getTempDtos() {
        return tempDtos;
    }

    public void setTempDtos(List<CtDvMapTempDto> tempDtos) {
        this.tempDtos = tempDtos;
    }
}
