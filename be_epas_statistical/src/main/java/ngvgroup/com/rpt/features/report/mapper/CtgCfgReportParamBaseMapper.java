package ngvgroup.com.rpt.features.report.mapper;


import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseDto;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparambase.CtgCfgReportParamBaseResponse;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParamBase;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CtgCfgReportParamBaseMapper {
    CtgCfgReportParamBase toEntity(CtgCfgReportParamBaseDto paramBaseDto);

    CtgCfgReportParamBaseDto toDto(CtgCfgReportParamBase paramBase);
    CtgCfgReportParamBaseResponse toResponse(CtgCfgReportParamBase paramBase);

    @Mapping(target = "paramBaseCode", ignore = true)
    void updateComCfgReportParamBase(CtgCfgReportParamBaseDto dto, @MappingTarget CtgCfgReportParamBase entity);


}
