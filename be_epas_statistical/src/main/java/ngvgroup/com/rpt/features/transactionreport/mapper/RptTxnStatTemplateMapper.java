package ngvgroup.com.rpt.features.transactionreport.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.transactionreport.dto.RptTxnStatTemplateDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface RptTxnStatTemplateMapper extends BaseMapper<RptTxnStatTemplateDto, RptTxnStatTemplate> {
    ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper INSTANCE= Mappers.getMapper(ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper.class);
}