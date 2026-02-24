package ngvgroup.com.rpt.features.report.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportgroupdto.CtgCfgReportGroupDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportGroupMapper extends BaseMapper<CtgCfgReportGroupDTO, CtgCfgReportGroup> {
}