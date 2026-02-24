package ngvgroup.com.rpt.features.ctgcfgstatscorerank.mapper;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkDto;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkExcel;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreBenchmarkRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreBenchmark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CtgCfgStatScoreBenchmarkMapper {
    @Mapping(source = "statScoreType.statScoreTypeName", target = "statScoreTypeName")
    StatScoreBenchmarkDto toDto(CtgCfgStatScoreBenchmark entity);

    CtgCfgStatScoreBenchmark toEntity(StatScoreBenchmarkRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    void updateEntityFromRequest(StatScoreBenchmarkRequest req, @MappingTarget CtgCfgStatScoreBenchmark entity);

    @Mapping(source = "statScoreType.statScoreTypeName", target = "statScoreTypeName")
    List<StatScoreBenchmarkExcel> toExcelDto(List<CtgCfgStatScoreBenchmark> entity);

}
