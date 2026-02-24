package ngvgroup.com.crm.features.common.repository;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;

import ngvgroup.com.crm.features.common.model.ComInfIndustry;
import ngvgroup.com.crm.features.common.dto.ComInfIndustryDto;

public interface ComInfIndustryRepository extends BaseRepository<ComInfIndustry> {

    @Query("""
        SELECT new ngvgroup.com.crm.features.common.dto.ComInfIndustryDto(
            e.industryCode,
            e.industryName
        )
        FROM ComInfIndustry e
        WHERE e.isDelete = 0
        ORDER BY e.industryCode ASC
        """)
    List<ComInfIndustryDto> findAllDto();
}