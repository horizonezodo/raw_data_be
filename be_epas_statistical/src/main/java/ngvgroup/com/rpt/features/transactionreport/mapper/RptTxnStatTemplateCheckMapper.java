package ngvgroup.com.rpt.features.transactionreport.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.rpt.features.transactionreport.dto.RptTxnStatTemplateCheckDto;
import ngvgroup.com.rpt.features.transactionreport.model.RptTxnStatTemplateCheck;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RptTxnStatTemplateCheckMapper extends BaseMapper<RptTxnStatTemplateCheckDto, RptTxnStatTemplateCheck> {
    ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper INSTANCE= Mappers.getMapper(ngvgroup.com.rpt.features.common.mapper.ComCfgCommonMapper.class);
}