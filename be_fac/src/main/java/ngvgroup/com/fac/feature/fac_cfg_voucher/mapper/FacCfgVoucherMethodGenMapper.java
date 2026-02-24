package ngvgroup.com.fac.feature.fac_cfg_voucher.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherMethodGenDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherMethodGen;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgVoucherMethodGenMapper extends BaseMapper<FacCfgVoucherMethodGenDTO, FacCfgVoucherMethodGen> {
}
