package ngvgroup.com.fac.feature.common.repository;
import jdk.jfr.Registered;
import ngvgroup.com.fac.feature.common.dto.CtgInfCurrencyTypeDto;
import ngvgroup.com.fac.feature.common.model.CtgInfCurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Registered
@Repository
public interface CtgInfCurrencyTypeRepository extends JpaRepository<CtgInfCurrencyType, Long> {

    @Query("SELECT new ngvgroup.com.fac.feature.common.dto.CtgInfCurrencyTypeDto(" +
            "c.currencyCode,c.currencyName) " +
            "FROM CtgInfCurrencyType c " +
            "WHERE c.isActive=1")
    List<CtgInfCurrencyTypeDto> getAllCtgInfCurrencyTypes();
}
