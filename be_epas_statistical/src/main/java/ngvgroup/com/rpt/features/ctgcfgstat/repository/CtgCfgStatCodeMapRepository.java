package ngvgroup.com.rpt.features.ctgcfgstat.repository;

import ngvgroup.com.rpt.features.ctgcfgstat.model.CtgCfgStatCodeMap;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgCfgStatCodeMapRepository extends CrudRepository<CtgCfgStatCodeMap, Long> {
    List<CtgCfgStatCodeMap> findByStatTypeCodeAndStatRegulatoryCode(String statTypeCode, String statRegulatoryCode);

    List<CtgCfgStatCodeMap> findByStatTypeCodeAndStatInternalCodeIn(String statTypeCode, List<String> statInternalCodes);

    Optional<CtgCfgStatCodeMap> findByStatTypeCodeAndStatInternalCode(String statTypeCode, String statInternalCode);

    @Query("""
        select m.statInternalCode, m.statRegulatoryCode
            from CtgCfgStatCodeMap m
            where m.statInternalCode in :statInternalCode
            and m.statTypeCode = :statTypeCode
""")
    List<Object[]> getStatRegulatoryCodes(@Param("statInternalCode") List<String> statInternalCode, @Param("statTypeCode") String statTypeCode);
}