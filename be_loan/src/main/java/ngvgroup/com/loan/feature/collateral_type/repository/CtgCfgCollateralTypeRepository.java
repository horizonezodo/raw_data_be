package ngvgroup.com.loan.feature.collateral_type.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeResponse;
import ngvgroup.com.loan.feature.collateral_type.model.CtgCfgCollateralType;

import java.util.List;

@Repository
public interface CtgCfgCollateralTypeRepository extends JpaRepository<CtgCfgCollateralType,Long>  {
    @Query("select new ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeResponse(ccct.id, ccct.collateralTypeCode, ccct.collateralTypeName, ccct.deductionRatio, ccct.guaranteeRatio, " +
            "ccct.riskCoefficient, ccct.isActive) from CtgCfgCollateralType ccct " +
            "where ccct.isDelete = 0 and (:filter is null or lower(ccct.collateralTypeCode) ilike concat('%', lower(:filter), '%') " +
            "or lower(ccct.collateralTypeName) ilike concat('%', lower(:filter), '%') or to_char(ccct.deductionRatio) ilike concat('%', lower(:filter), '%') " +
            "or to_char(ccct.guaranteeRatio) ilike concat('%', lower(:filter), '%') or to_char(ccct.riskCoefficient) ilike concat('%', lower(:filter), '%') " +
            "or (ccct.isActive = 1 and 'hiệu lực' ilike concat('%', lower(:filter), '%')) or (ccct.isActive = 0 and 'hết hiệu lực' ilike concat('%', lower(:filter), '%')))")
    Page<CtgCfgCollateralTypeResponse> search(@Param("filter") String filter, Pageable pageable);

    @Query("select new ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeResponse(ccct.id, ccct.collateralTypeCode, ccct.collateralTypeName, ccct.deductionRatio, ccct.guaranteeRatio, " +
            "ccct.riskCoefficient, ccct.isActive) from CtgCfgCollateralType ccct " +
            "where ccct.isDelete = 0 and (:filter is null or lower(ccct.collateralTypeCode) ilike concat('%', lower(:filter), '%') " +
            "or lower(ccct.collateralTypeName) ilike concat('%', lower(:filter), '%') or to_char(ccct.deductionRatio) ilike concat('%', lower(:filter), '%') " +
            "or to_char(ccct.guaranteeRatio) ilike concat('%', lower(:filter), '%') or to_char(ccct.riskCoefficient) ilike concat('%', lower(:filter), '%') " +
            "or (ccct.isActive = 1 and 'hiệu lực' ilike concat('%', lower(:filter), '%')) or (ccct.isActive = 0 and 'hết hiệu lực' ilike concat('%', lower(:filter), '%')))")
    List<CtgCfgCollateralTypeResponse> exportToExcel(@Param("filter") String filter );

    List<CtgCfgCollateralType> findCtgCfgCollateralTypeByCollateralTypeCodeAndIsDelete(String collateralTypeCode, int isDelete);
}
