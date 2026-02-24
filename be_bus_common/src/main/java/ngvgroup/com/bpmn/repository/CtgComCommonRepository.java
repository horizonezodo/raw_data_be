package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.dto.ComCommon.CommonDto;
import ngvgroup.com.bpmn.model.CtgComCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface CtgComCommonRepository extends JpaRepository<CtgComCommon, Long> {

    @Query("Select new ngvgroup.com.bpmn.dto.ComCommon.CommonDto(" +
            "cm.commonCode, cm.commonName) " +
            "FROM CtgComCommon cm " +
            "WHERE cm.commonTypeCode = :commonTypeCode")
    List<CommonDto> listCommon(@RequestParam("commonTypeCode") String commonTypeCode);

    @Query("Select distinct new ngvgroup.com.bpmn.dto.ComCommon.CommonDto(" +
            "cm.commonCode, cm.commonName, cm.parentCode) " +
            "FROM CtgComCommon cm ")
    List<CommonDto> getAllCommon();

     @Query("SELECT c.commonName from CtgComCommon c where c.commonCode = :commonCode")
    String getCommonName(@Param("commonCode")String commonCode);

    List<CtgComCommon> findAllByCommonTypeCode(String commonTypeCode);
}
