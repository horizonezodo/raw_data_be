package com.naas.category_service.repository;


import com.naas.category_service.dto.ComCommon.CommonDto;
import com.naas.category_service.model.CtgComCommon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface CtgComCommonRepository extends JpaRepository<CtgComCommon, Long> {

    @Query("Select new com.naas.category_service.dto.ComCommon.CommonDto(" +
            "cm.commonCode, cm.commonName, cm.parentCode) " +
            "FROM CtgComCommon cm " +
            "WHERE cm.commonTypeCode = :commonTypeCode")
    List<CommonDto> listCommon(@RequestParam("commonTypeCode") String commonTypeCode);
    
}
