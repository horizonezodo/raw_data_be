package com.naas.category_service.repository;

import com.naas.category_service.dto.CtgInfCurrencyType.CtgInfCurrencyTypeDto;
import com.naas.category_service.model.CtgInfCurrencyType;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Registered
public interface CtgInfCurrencyTypeRepository extends JpaRepository<CtgInfCurrencyType, Long> {

    @Query("SELECT new com.naas.category_service.dto.CtgInfCurrencyType.CtgInfCurrencyTypeDto(" +
            "c.currencyCode,c.currencyName) " +
            "FROM CtgInfCurrencyType c " +
            "WHERE c.isActive=true")
    List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes();
}
