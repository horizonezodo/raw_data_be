package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeResponse;
import com.naas.category_service.model.CtgCfgCollateralType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgCollateralTypeRepository extends JpaRepository<CtgCfgCollateralType,Long>  {
    @Query("select new com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeResponse(ccct.id, ccct.collateralTypeCode, ccct.collateralTypeName, ccct.deductionRatio, ccct.guaranteeRatio, " +
            "ccct.riskCoefficient, null, ccct.isActive) from CtgCfgCollateralType ccct " +
            "where ccct.isDelete = 0 and (:filter is null or lower(ccct.collateralTypeCode) ilike %:filter% " +
            "or lower(ccct.collateralTypeName) ilike %:filter% or to_char(ccct.deductionRatio) ilike %:filter% " +
            "or to_char(ccct.guaranteeRatio) ilike %:filter% or to_char(ccct.riskCoefficient) ilike %:filter% " +
            "or (ccct.isActive = true and 'hiệu lực' ilike %:filter%) or (ccct.isActive = false and 'hết hiệu lực' ilike %:filter%))")
    Page<CtgCfgCollateralTypeResponse> search(String filter, Pageable pageable);

    @Query("select new com.naas.category_service.dto.CtgCfgCollateralType.CtgCfgCollateralTypeResponse(ccct.id, ccct.collateralTypeCode, ccct.collateralTypeName, ccct.deductionRatio, ccct.guaranteeRatio, " +
            "ccct.riskCoefficient, null, ccct.isActive) from CtgCfgCollateralType ccct " +
            "where ccct.isDelete = 0 and (:filter is null or lower(ccct.collateralTypeCode) ilike %:filter% " +
            "or lower(ccct.collateralTypeName) ilike %:filter% or to_char(ccct.deductionRatio) ilike %:filter% " +
            "or to_char(ccct.guaranteeRatio) ilike %:filter% or to_char(ccct.riskCoefficient) ilike %:filter% " +
            "or (ccct.isActive = true and 'hiệu lực' ilike %:filter%) or (ccct.isActive = false and 'hết hiệu lực' ilike %:filter%))")
    List<CtgCfgCollateralTypeResponse> exportToExcel(@Param("filter") String filter );

    List<CtgCfgCollateralType> findCtgCfgCollateralTypeByCollateralTypeCodeAndIsDelete(String collateralTypeCode, int isDelete);
}
