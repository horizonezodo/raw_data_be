package ngvgroup.com.bpm.features.common.mapper;


import ngvgroup.com.bpm.features.common.dto.CtgComCommonDTO;
import ngvgroup.com.bpm.features.common.model.CtgComCommon;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgComCommonMapper {
    CtgComCommonDTO toDto(CtgComCommon ctgComCommon);
}