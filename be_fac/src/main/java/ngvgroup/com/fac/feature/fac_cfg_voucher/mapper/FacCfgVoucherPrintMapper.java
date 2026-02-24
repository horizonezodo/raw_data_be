package ngvgroup.com.fac.feature.fac_cfg_voucher.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.fac.feature.fac_cfg_voucher.dto.FacCfgVoucherPrintDTO;
import ngvgroup.com.fac.feature.fac_cfg_voucher.model.FacCfgVoucherPrint;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FacCfgVoucherPrintMapper extends BaseMapper<FacCfgVoucherPrintDTO, FacCfgVoucherPrint> {

}
