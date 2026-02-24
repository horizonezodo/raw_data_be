package ngvgroup.com.fac.feature.fac_inf_acc.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import feign.Param;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacCfgAccMapValue;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacCfgAccMapValueRepository extends BaseRepository<FacCfgAccMapValue> {
    List<FacCfgAccMapValue> findByMapCode(String mapCode);

    @Query("""
        select m
        from FacCfgAccMapValue m
        where m.mapCode = :mapCode
          and m.businessCode = :businessCode
    """)
    Optional<FacCfgAccMapValue> findByMapCodeAndBusinessCode(
            @Param("mapCode") String mapCode,
            @Param("businessCode") String businessCode
    );

    @Query("""
        select m
        from FacCfgAccMapValue m
        where m.mapCode = :mapCode
          and m.orgCode = :orgCode
    """)
    Optional<FacCfgAccMapValue> findByMapValueAndOrgCode(
            @Param("mapCode") String mapCode,
            @Param("orgCode") String orgCode
    );
}
