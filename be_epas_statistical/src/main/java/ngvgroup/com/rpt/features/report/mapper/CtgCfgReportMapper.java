package ngvgroup.com.rpt.features.report.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreport.CtgCfgReportDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReport;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportMapper extends BaseMapper<CtgCfgReportDTO, CtgCfgReport> {
}