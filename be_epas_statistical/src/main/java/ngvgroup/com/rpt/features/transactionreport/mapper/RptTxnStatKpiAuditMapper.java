package ngvgroup.com.rpt.features.transactionreport.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.transactionreport.dto.KpiAuditRequestDto;
import ngvgroup.com.rpt.features.transactionreport.dto.RptTxnStatKpiAuditDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatKpiAudit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RptTxnStatKpiAuditMapper extends BaseMapper<RptTxnStatKpiAuditDto, RptTxnStatKpiAudit> {
    ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper INSTANCE= Mappers.getMapper(ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper.class);

    RptTxnStatKpiAudit toEntity(KpiAuditRequestDto dto);

    List<RptTxnStatKpiAudit> toEntityListFromRequest(List<KpiAuditRequestDto> dtoList);
}