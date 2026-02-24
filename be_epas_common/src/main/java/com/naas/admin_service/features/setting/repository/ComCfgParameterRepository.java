package com.naas.admin_service.features.setting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.naas.admin_service.features.setting.dto.ComCfgParameterResponse;
import com.naas.admin_service.features.setting.model.ComCfgParameter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComCfgParameterRepository extends JpaRepository<ComCfgParameter, Long> {

    @Query("select distinct new com.naas.admin_service.features.setting.dto.ComCfgParameterResponse(" +
            "ccp.id, ccp.paramCode, ccp.paramName, ccp.paramValue, ccp.valueDescription, ccp.orgCode) " +
            "from ComCfgParameter ccp " +
            "where ccp.isActive = 1 " +
            "and ( :filter is null " +
            "or lower(ccp.paramCode) like lower(concat('%', :filter, '%')) " +
            "or lower(ccp.paramName) like lower(concat('%', :filter, '%')) " +
            "or lower(ccp.paramValue) like lower(concat('%', :filter, '%')) " +
            "or lower(ccp.valueDescription) like lower(concat('%', :filter, '%')) " +
            "or (ccp.orgCode = '%' and lower('Tất cả') like lower(concat('%', :filter, '%'))) )")
    List<ComCfgParameterResponse> listComCfgParameters(@Param("filter")String filter);

    // Thêm phương thức tìm theo paramCode, isActive = true, isDelete = false
    Optional<ComCfgParameter> findByParamCodeAndIsActiveTrueAndIsDeleteFalse(String paramCode);

    List<ComCfgParameter>findAllByParamCodeAndParamValue(String paramCode,String paramValue);
}
