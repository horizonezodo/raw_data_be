package ngvgroup.com.ibm.feature.dep_product.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDtlDTO;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProductDtl;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IbmCfgDepProductDtlMapper extends BaseMapper<IbmCfgDepProductDtlDTO, IbmCfgDepProductDtl> {
}