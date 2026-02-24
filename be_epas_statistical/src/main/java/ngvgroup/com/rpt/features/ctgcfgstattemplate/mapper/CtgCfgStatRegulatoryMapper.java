package ngvgroup.com.rpt.features.ctgcfgstattemplate.mapper;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.StatRegulatoryExcelDto;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatory.CtgCfgStatRegulatoryResponse;
import ngvgroup.com.rpt.features.ctgcfgstattemplate.model.CtgCfgStatRegulatory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CtgCfgStatRegulatoryMapper {
    @Mapping(source = "statType.statTypeName", target = "statTypeName")
    @Mapping(source = "common.commonCode", target = "commonCode")
    @Mapping(source = "common.commonName", target = "commonName")
    @Mapping(source = "description", target = "description")
    CtgCfgStatRegulatoryDto toDto (CtgCfgStatRegulatory entity);
    CtgCfgStatRegulatoryResponse entityToDto (CtgCfgStatRegulatory entity);

    @Mapping(source = "statType.statTypeName", target = "statTypeName")
    List<StatRegulatoryExcelDto> entiToDto (List<CtgCfgStatRegulatory> entity);
}
