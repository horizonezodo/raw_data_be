package ngvgroup.com.rpt.features.transactionreport.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.transactionreport.dto.RptTxnStatTemplateStatusDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateStatus;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RptTxnStatTemplateStatusMapper extends BaseMapper<RptTxnStatTemplateStatusDto, RptTxnStatTemplateStatus> {
    ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper INSTANCE= Mappers.getMapper(ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper.class);
}