package ngvgroup.com.rpt.features.report.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;

import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparamcond.CtgCfgReportParamCondDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParamCond;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportParamCondMapper extends BaseMapper<CtgCfgReportParamCondDTO, CtgCfgReportParamCond> {
}