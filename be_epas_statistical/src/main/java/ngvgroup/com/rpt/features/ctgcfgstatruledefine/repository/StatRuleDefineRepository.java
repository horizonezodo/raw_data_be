package ngvgroup.com.rpt.features.ctgcfgstatruledefine.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ResponseSearchStatRuleDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatruledefine.model.StatRuleDefine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StatRuleDefineRepository extends BaseRepository<StatRuleDefine> {

    Optional<StatRuleDefine> findByRuleCode(String name);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgstatruledefine.dto.ResponseSearchStatRuleDefineDto(
                    rd.id,
                    rd.ruleCode,
                    rd.ruleName,
                    rt.ruleTypeName,
                    rd.description
                )FROM StatRuleDefine rd
                LEFT JOIN StatRuleType rt ON rt.ruleTypeCode = rd.ruleTypeCode
                WHERE (
                    :search IS NULL OR
                    LOWER(rd.ruleCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(rd.ruleName) LIKE LOWER(CONCAT('%', :search, '%'))
                )
                AND :ruleTypeCodes IS NULL OR rd.ruleTypeCode IN :ruleTypeCodes
            """)
    Page<ResponseSearchStatRuleDefineDto> search(
            @Param("search") String search,
            @Param("ruleTypeCodes") List<String> ruleTypeCodes,
            Pageable pageable);
}
