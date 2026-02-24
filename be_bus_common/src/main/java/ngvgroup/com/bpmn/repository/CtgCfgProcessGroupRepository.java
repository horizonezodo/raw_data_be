package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.dto.CtgCfgProcessGroup.CtgCfgProcessGroupDto;
import ngvgroup.com.bpmn.model.CtgCfgProcessGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtgCfgProcessGroupRepository extends JpaRepository<CtgCfgProcessGroup, Long> {

    @Query("SELECT new ngvgroup.com.bpmn.dto.CtgCfgProcessGroup.CtgCfgProcessGroupDto(" +
            "c.processGroupCode,c.processGroupName) " +
            "FROM CtgCfgProcessGroup c")
    List<CtgCfgProcessGroupDto> getListProcessGroup();
}
