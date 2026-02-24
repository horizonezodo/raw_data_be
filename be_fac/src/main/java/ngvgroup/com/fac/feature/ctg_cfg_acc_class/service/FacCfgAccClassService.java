package ngvgroup.com.fac.feature.ctg_cfg_acc_class.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FacCfgAccClassService extends BaseService<FacCfgAccClass, FacCfgAccClassDto> {
    Page<FacCfgAccClassDto> searchAll(String keyword, Pageable pageable);

    ResponseEntity<byte[]> exportToExcel(String keyword,String fileName);

    boolean existsByAccClassCode(String accClassCode);

    List<FacCfgAccClassDto> findAllByAccSideType(String accSideType);
}
