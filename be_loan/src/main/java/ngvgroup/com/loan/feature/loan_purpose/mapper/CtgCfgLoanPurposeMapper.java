package ngvgroup.com.loan.feature.loan_purpose.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import ngvgroup.com.loan.feature.loan_purpose.dto.CtgCfgLoanPurposeDto;
import ngvgroup.com.loan.feature.loan_purpose.model.CtgCfgLoanPurpose;

@Mapper(componentModel = "spring")
public interface CtgCfgLoanPurposeMapper extends BaseMapper<CtgCfgLoanPurposeDto, CtgCfgLoanPurpose>{
    @Mapping(target = "purposeCode", ignore = true)
    void updateCtgCfgLoanPurposeFromDto(CtgCfgLoanPurposeDto dto, @MappingTarget CtgCfgLoanPurpose entity);
}

