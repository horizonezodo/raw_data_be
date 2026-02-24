package ngvgroup.com.fac.feature.regulated_account.repository;

import ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaVersionDTO;
import ngvgroup.com.fac.feature.regulated_account.model.CtgCfgCoaVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgCoaVersionRepository extends JpaRepository<CtgCfgCoaVersion,Long> {
    @Query("""
SELECT new ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaVersionDTO(
    c.coaVersionCode,
    c.coaScope,
    c.isDefault,
    c.coaVersionName
)FROM CtgCfgCoaVersion c
WHERE c.isDefault=1
""")
    List<CtgCfgCoaVersionDTO> getAll();

    @Query("""
SELECT new ngvgroup.com.fac.feature.regulated_account.dto.CtgCfgCoaVersionDTO(
    c.coaVersionCode,
    c.coaScope,
    c.isDefault,
    c.coaVersionName,
    com.commonValue
)FROM CtgCfgCommon com,CtgCfgCoaVersion c
WHERE  com.commonTypeCode = :commonTypeCode AND  c.isDefault=1
""")
    List<CtgCfgCoaVersionDTO> getAllWithCommon(@Param("commonTypeCode")String commonTypeCode);
}
