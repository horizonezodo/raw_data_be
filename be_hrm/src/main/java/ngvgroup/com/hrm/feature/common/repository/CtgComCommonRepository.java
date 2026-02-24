package ngvgroup.com.hrm.feature.common.repository;
import ngvgroup.com.hrm.feature.common.dto.CommonDto;
import ngvgroup.com.hrm.feature.common.dto.CtgComCommonDTO;
import ngvgroup.com.hrm.feature.common.model.CtgComCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public interface CtgComCommonRepository extends JpaRepository<CtgComCommon, Long> {
    List<CtgComCommon> findAllByCommonTypeCode(String commonTypeCode);
    @Query("Select new ngvgroup.com.hrm.feature.common.dto.CtgComCommonDTO(" +
            "cm.commonCode, cm.commonName, cm.parentCode, cm.commonValue) " +
            "FROM CtgComCommon cm " +
            "WHERE cm.commonTypeCode = :commonTypeCode")
    List<CtgComCommonDTO> listCommon(@RequestParam("commonTypeCode") String commonTypeCode);

    @Query("SELECT c.commonValue from CtgComCommon c where c.commonCode=:commonCode")
    String getSqlQuery(@Param("commonCode")String commonCode);

    @Query("SELECT c.commonName FROM CtgComCommon c WHERE c.commonCode = :code")
    Optional<String> findCommonNameByCode(String code);

    @Query("""
        Select new ngvgroup.com.hrm.feature.common.dto.CommonDto(
            commonCode, commonName
            ) from CtgComCommon where commonTypeCode in :commonTypeCodes
    """)
    List<CommonDto> findByCommonTypeCodeIn(List<String> commonTypeCodes);
}
