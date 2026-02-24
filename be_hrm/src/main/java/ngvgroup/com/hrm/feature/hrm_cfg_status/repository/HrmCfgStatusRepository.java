package ngvgroup.com.hrm.feature.hrm_cfg_status.repository;

import com.ngvgroup.bpm.core.persistence.repository.BaseRepository;
import ngvgroup.com.hrm.feature.hrm_cfg_status.model.HrmCfgStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HrmCfgStatusRepository extends BaseRepository<HrmCfgStatus> {
    List<HrmCfgStatus> findAllByIsDelete(int isDelete);
}
