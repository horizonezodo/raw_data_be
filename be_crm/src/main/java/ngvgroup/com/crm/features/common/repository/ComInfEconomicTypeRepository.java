package ngvgroup.com.crm.features.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.model.ComInfEconomicType;
import ngvgroup.com.crm.features.common.dto.ComInfEconomicTypeDto;

public interface ComInfEconomicTypeRepository extends BaseRepository<ComInfEconomicType> {

    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfEconomicTypeDto(
            e.economicTypeCode,
            e.economicTypeName
        )
        FROM ComInfEconomicType e
        WHERE e.isDelete = 0
        ORDER BY e.economicTypeCode ASC
        """)
    List<ComInfEconomicTypeDto> findAllDto();
}