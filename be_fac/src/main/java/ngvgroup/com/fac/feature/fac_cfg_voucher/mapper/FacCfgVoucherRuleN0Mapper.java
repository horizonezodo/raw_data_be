package ngvgroup.com.fac.feature.fac_cfg_voucher.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherRuleN0DTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherRuleN0;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgVoucherRuleN0Mapper extends BaseMapper<FacCfgVoucherRuleN0DTO, FacCfgVoucherRuleN0> {

}
