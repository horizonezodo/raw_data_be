package ngvgroup.com.loan.feature.interest_process.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.loan.feature.interest_process.dto.CfgInterestRateDTO;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.dto.LnmCfgIntRateDTO;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRate;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateTier;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRate;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateTier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LnmTxnIntRateMapper extends BaseMapper<InterestProfileDTO, LnmTxnIntRate> {
    LnmTxnIntRateMapper INSTANCE = Mappers.getMapper(LnmTxnIntRateMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "businessStatus", constant = "ACTIVE")
    @Override
    LnmTxnIntRate toEntity(InterestProfileDTO dto);

    LnmCfgIntRate toCfgEntity(InterestProfileDTO dto);

    InterestProfileDTO toDto(LnmTxnIntRate entity);

    LnmTxnIntRate toTxn(LnmCfgIntRate cfgIntRate);

    LnmCfgIntRate toCfg(LnmTxnIntRate txnIntRate);

    LnmCfgIntRateFixed toFixedCfg(LnmTxnIntRateFixed txnIntRateFixed);

    LnmCfgIntRateTier toTierCfg(LnmTxnIntRateTier txnIntRateTier);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "modifiedDate", ignore = true)
    void updateCfg(
            LnmTxnIntRate txnIntRate,
            @MappingTarget LnmCfgIntRate cfgIntRate
    );

    // UPDATE
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "processTypeCode", ignore = true)
    @Mapping(target = "businessStatus", ignore = true)
    @Mapping(target = "processInstanceCode", ignore = true)
    void updateTxn(
            InterestProfileDTO dto,
            @MappingTarget LnmTxnIntRate entity
    );

    CfgInterestRateDTO toCfgDTO(LnmCfgIntRate cfgIntRate);

    List<LnmCfgIntRateDTO> toListCfgDTO(List<LnmCfgIntRate> all);
}
