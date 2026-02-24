package ngvgroup.com.loan.feature.ward.mapper;

import ngvgroup.com.loan.feature.ward.dto.CtgComWardDto;
import ngvgroup.com.loan.feature.ward.model.CtgComWard;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CtgComWardMapper {
    CtgComWardDto toDto(CtgComWard ctgComWard);
}
