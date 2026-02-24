package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto;
import ngvgroup.com.bpmn.model.ComCfgProcessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCfgProcessTypeRepository extends JpaRepository<ComCfgProcessType, Long> {
    @Query("SELECT new ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto(" +
            "c.processTypeCode,c.processTypeName,c.processGroupCode) " +
            "FROM ComCfgProcessType c")
    List<ComCfgProcessTypeDto> getListProcessType();

    @Query("SELECT new ngvgroup.com.bpmn.dto.ComCfgProcessType.ComCfgProcessTypeDto(" +
            "c.processTypeCode,c.processTypeName,c.processGroupCode) " +
            "FROM ComCfgProcessType  c " +
            "WHERE c.processGroupCode=:processGroupCode")
    List<ComCfgProcessTypeDto> getListProcessTypeByGroupCode(@Param("processGroupCode")String processGroupCode);

}
