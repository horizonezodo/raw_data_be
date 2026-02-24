package ngvgroup.com.fac.feature.common.repository;

import ngvgroup.com.fac.feature.common.dto.ComCfgProcessTypeDTO;
import ngvgroup.com.fac.feature.common.model.ComCfgProcessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComCfgProcessTypeRepository extends JpaRepository<ComCfgProcessType, Long> {
    @Query("""
                select new ngvgroup.com.fac.feature.common.dto.ComCfgProcessTypeDTO(
                    type.processTypeCode, type.processTypeName )
                from ComCfgProcessType type
                where type.isAccounting = 1
            """)
    List<ComCfgProcessTypeDTO> getAll();
}
