package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.ResponseSearchStatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.model.StatResponseDefine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StatResponseDefineRepository extends BaseRepository<StatResponseDefine> {
    Optional<StatResponseDefine> findByResponseCode(String name);
    boolean existsByResponseCode(String ruleTypeCode);

    @Query("""
                SELECT new ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.ResponseSearchStatResponseDefineDto(
                    rt.id,
                    rt.responseCode,
                    rt.responseName,
                    rt.description
                )FROM StatResponseDefine rt
                WHERE (
                    :search IS NULL OR
                    LOWER(rt.responseCode) LIKE LOWER(CONCAT('%', :search, '%')) OR
                    LOWER(rt.responseName) LIKE LOWER(CONCAT('%', :search, '%'))
                )
            """)
    Page<ResponseSearchStatResponseDefineDto> search(
            @Param("search") String search,
            Pageable pageable);
}
