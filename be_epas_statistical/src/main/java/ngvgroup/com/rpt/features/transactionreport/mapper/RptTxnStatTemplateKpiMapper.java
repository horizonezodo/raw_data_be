package ngvgroup.com.rpt.features.transactionreport.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.transactionreport.dto.RptTxnStatTemplateKpiDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateKpi;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface RptTxnStatTemplateKpiMapper extends BaseMapper<RptTxnStatTemplateKpiDto, RptTxnStatTemplateKpi> {
    ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper INSTANCE= Mappers.getMapper(ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper.class);
}