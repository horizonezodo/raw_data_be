package ngvgroup.com.fac.feature.common.repository;
import ngvgroup.com.fac.feature.common.dto.ComCfgProcessFileDto;
import ngvgroup.com.fac.feature.common.model.ComCfgProcessFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCfgProcessFileRepository extends JpaRepository<ComCfgProcessFile, Long> {
    @Query("SELECT new ngvgroup.com.fac.feature.common.dto.ComCfgProcessFileDto(" +
            "c.processFileCode,c.processFileName) " +
            "FROM ComCfgProcessFile c " +
            "WHERE c.processTypeCode=:processTypeCode")
    List<ComCfgProcessFileDto> getByProcessTypeCode(@Param("processTypeCode") String processTypeCode);
}
