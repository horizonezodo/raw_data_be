package ngvgroup.com.loan.feature.product_proccess.mapper;

import com.ngvgroup.bpm.core.persistence.mapper.BaseMapper;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.product_proccess.dto.LnmTxnProductDtlDTO;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProduct;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProductDtl;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProduct;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProductDtl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface LnmTxnProductMapper extends BaseMapper<ProductProfileDTO, LnmTxnProduct> {
    LnmTxnProductMapper INSTANCE = Mappers.getMapper(LnmTxnProductMapper.class);

    @Mapping(target = "processTypeCode", constant = LoanVariableConstants.PREFIX_PRODUCT_REGISTER)
    @Mapping(target = "businessStatus", constant = LoanVariableConstants.ACTIVE)
    @Override
    LnmTxnProduct toEntity(ProductProfileDTO dto);

    @Mapping(target = "productDetails", ignore = true)
    @Override
    ProductProfileDTO toDto(LnmTxnProduct product);

    @Mapping(target = "id", ignore = true)
    LnmCfgProduct toCfg(LnmTxnProduct txnProduct);

    @Mapping(target = "id", ignore = true)
    LnmCfgProductDtl toCfgDtl(LnmTxnProductDtl txnProductDtl);

    @Mapping(target = "id", ignore = true)
    void updateCfg(LnmTxnProduct txnProduct,
                   @MappingTarget LnmCfgProduct cfgProduct);

    LnmTxnProductDtlDTO toDtlDTO(LnmTxnProductDtl detail);

    @Mapping(target = "id", ignore = true)
    void updateDtlEntity(LnmTxnProductDtlDTO dto, @MappingTarget LnmTxnProductDtl detail);

    void updateTxn(ProductProfileDTO dto, @MappingTarget LnmTxnProduct entity);

    LnmTxnProductDtlDTO toDto(LnmCfgProductDtl detail);

    @Mapping(target = "productDetails", ignore = true)
    ProductProfileDTO fromCfgToDto(LnmCfgProduct product);
}
