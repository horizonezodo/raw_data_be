package ngvgroup.com.rpt.features.ctgcfgstatruletype.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.ResponseSearchStatRuleTypeDto;
import ngvgroup.com.rpt.features.ctgcfgstatruletype.model.StatRuleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatRuleTypeRepository extends BaseRepository<StatRuleType> {
    Optional<StatRuleType> findByRuleTypeCode(String name);
    boolean existsByRuleTypeCode(String ruleTypeCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgstatruletype.dto.ResponseSearchStatRuleTypeDto(
                    rt.id,
                    rt.ruleTypeCode,
                    rt.ruleTypeName,
                    rt.description
                )FROM StatRuleType rt
                WHERE (
                    :search IS NULL OR
                    LOWER(rt.ruleTypeCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(rt.ruleTypeName) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            """)
    Page<ResponseSearchStatRuleTypeDto> search(
            @Param("search") String search,
            Pageable pageable);
}
