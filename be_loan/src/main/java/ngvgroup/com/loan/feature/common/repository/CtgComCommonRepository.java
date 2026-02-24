package ngvgroup.com.loan.feature.common.repository;

import ngvgroup.com.loan.feature.common.dto.CommonDto;
import ngvgroup.com.loan.feature.common.model.CtgComCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface CtgComCommonRepository extends JpaRepository<CtgComCommon, Long> {
    @Query("Select distinct new ngvgroup.com.loan.feature.common.dto.CommonDto(" +
            "cm.commonCode, cm.commonName, cm.parentCode) " +
            "FROM CtgComCommon cm ")
    List<CommonDto> getAllCommon();

    @Query("Select new ngvgroup.com.loan.feature.common.dto.CommonDto(" +
            "cm.commonCode, cm.commonName, cm.parentCode, cm.commonValue) " +
            "FROM CtgComCommon cm " +
            "WHERE cm.commonTypeCode = :commonTypeCode")
    List<CommonDto> listCommon(@RequestParam("commonTypeCode") String commonTypeCode);

    List<CtgComCommon> findAllByCommonTypeCode(String commonTypeCode);
}
