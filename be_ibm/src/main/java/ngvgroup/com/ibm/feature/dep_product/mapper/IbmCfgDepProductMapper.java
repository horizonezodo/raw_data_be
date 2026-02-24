package ngvgroup.com.ibm.feature.dep_product.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDTO;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IbmCfgDepProductMapper extends BaseMapper<IbmCfgDepProductDTO, IbmCfgDepProduct> {

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(IbmCfgDepProductDTO dto, @MappingTarget IbmCfgDepProduct entity);

}