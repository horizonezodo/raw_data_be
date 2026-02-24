package ngvgroup.com.loan.feature.currency.repository;

import jdk.jfr.Registered;
import ngvgroup.com.loan.feature.currency.dto.CtgInfCurrencyTypeDto;
import ngvgroup.com.loan.feature.currency.model.CtgInfCurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Registered
public interface CtgInfCurrencyTypeRepository extends JpaRepository<CtgInfCurrencyType, Long> {

    @Query("SELECT new ngvgroup.com.loan.feature.currency.dto.CtgInfCurrencyTypeDto(" +
            "c.currencyCode,c.currencyName) " +
            "FROM CtgInfCurrencyType c " +
            "WHERE c.isActive=1")
    List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes();
}
