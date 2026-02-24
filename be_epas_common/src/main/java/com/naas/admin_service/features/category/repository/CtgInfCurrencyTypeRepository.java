package com.naas.admin_service.features.category.repository;

import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.naas.admin_service.features.category.dto.CtgInfCurrencyTypeDto;
import com.naas.admin_service.features.category.model.CtgInfCurrencyType;

import java.util.List;

@Registered
public interface CtgInfCurrencyTypeRepository extends JpaRepository<CtgInfCurrencyType, Long> {

    @Query("SELECT new com.naas.admin_service.features.category.dto.CtgInfCurrencyTypeDto(" +
            "c.currencyCode,c.currencyName) " +
            "FROM CtgInfCurrencyType c " +
            "WHERE c.isActive=1")
    List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes();
}
