package ngvgroup.com.rpt.features.report.mapper;


import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.report.dto.ctgcfgreportparam.CtgCfgReportParamDTO;
import ngvgroup.com.rpt.features.report.model.CtgCfgReportParam;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgCfgReportParamMapper extends BaseMapper<CtgCfgReportParamDTO, CtgCfgReportParam> {
}