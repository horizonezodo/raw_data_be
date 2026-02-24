package ngvgroup.com.bpmn.repository;

import ngvgroup.com.bpmn.model.CtgCfgResourceMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CtgCfgResourceMappingRepository extends JpaRepository<CtgCfgResourceMapping, Long> {
    // Thêm custom query method nếu cần
    void deleteByResourceCode(String resourceCode);
}