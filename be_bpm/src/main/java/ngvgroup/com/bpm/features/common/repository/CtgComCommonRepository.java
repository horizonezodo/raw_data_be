package ngvgroup.com.bpm.features.common.repository;

import ngvgroup.com.bpm.features.common.dto.CommonDto;
import ngvgroup.com.bpm.features.common.model.CtgComCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.Collection;
import java.util.List;

@Repository
public interface CtgComCommonRepository extends JpaRepository<CtgComCommon, Long> {

    @Query("SELECT new ngvgroup.com.bpm.features.common.dto.CommonDto(" +
            "c.commonCode,c.commonName) " +
            "FROM CtgComCommon c " +
            "WHERE c.commonTypeCode= 'CM033'")
    List<CommonDto> getUnit();

    @Query("SELECT new ngvgroup.com.bpm.features.common.dto.CommonDto(" +
            "c.commonCode,c.commonName) " +
            "FROM CtgComCommon c " +
            "WHERE c.commonTypeCode= 'CM034'")
    List<CommonDto> getPriorityLevel();

    List<CtgComCommon> findAllByCommonTypeCode(String commonTypeCode);

    List<CtgComCommon> findAllByCommonCodeIn(Collection<String> commonCodes);
}
