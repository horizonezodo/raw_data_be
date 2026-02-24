package ngvgroup.com.rpt.features.ctgcfgstatkpi.mapper;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.dto.CtgCfgStatTypeKpiDto;
import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.ctgcfgstatkpi.model.CtgCfgStatTypeKpi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface CtgCfgStatTypeKpiMapper extends BaseMapper<CtgCfgStatTypeKpiDto, CtgCfgStatTypeKpi> {
    CtgCfgStatTypeKpiMapper INSTANCE= Mappers.getMapper(CtgCfgStatTypeKpiMapper.class);
}
