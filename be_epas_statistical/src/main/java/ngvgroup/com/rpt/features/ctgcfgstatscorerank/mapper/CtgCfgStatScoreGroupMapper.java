package ngvgroup.com.rpt.features.ctgcfgstatscorerank.mapper;

import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupRequest;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.CtgCfgStatScoreGroupResponse;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.dto.StatScoreGroupExcel;
import ngvgroup.com.rpt.features.ctgcfgstatscorerank.model.CtgCfgStatScoreGroup;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CtgCfgStatScoreGroupMapper {
    @Mapping(source = "statScoreType.statScoreTypeName", target = "statScoreTypeName")
    CtgCfgStatScoreGroupResponse toDto(CtgCfgStatScoreGroup entity);

    @Mapping(source = "statScoreGroupTypeName", target = "statScoreGroupTypeName")
    CtgCfgStatScoreGroup toEntity(StatScoreGroupRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    void updateEntityFromRequest(StatScoreGroupRequest req, @MappingTarget CtgCfgStatScoreGroup entity);

    @Mapping(source = "statScoreType.statScoreTypeName", target = "statScoreTypeName")
    List<StatScoreGroupExcel> toExcelDto(List<CtgCfgStatScoreGroup> entity);


}
