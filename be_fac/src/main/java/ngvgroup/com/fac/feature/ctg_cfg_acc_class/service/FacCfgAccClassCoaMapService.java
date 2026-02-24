package ngvgroup.com.fac.feature.ctg_cfg_acc_class.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapResDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClassCoaMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacCfgAccClassCoaMapService extends BaseService<FacCfgAccClassCoaMap, FacCfgAccClassCoaMapDto>{

    Page<FacCfgAccClassCoaMapDto> searchAll(String keyword,String accClassCode,Pageable pageable);

    void create(List<FacCfgAccClassCoaMapDto> facCfgAccClassCoaMapDtos);

    void update(List<FacCfgAccClassCoaMapDto> facCfgAccClassCoaMapDtos,String accClassCode);

    FacCfgAccClassCoaMapDto getByOrgCodeAndAccClassCode(String orgCode,String accClassCode);

    void delete(String accClassCode);
}
