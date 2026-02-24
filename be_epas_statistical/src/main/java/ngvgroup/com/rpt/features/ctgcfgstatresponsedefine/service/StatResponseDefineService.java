package ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.ResponseSearchStatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.dto.StatResponseDefineDto;
import ngvgroup.com.rpt.features.ctgcfgstatresponsedefine.model.StatResponseDefine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StatResponseDefineService extends BaseService<StatResponseDefine, StatResponseDefineDto> {
    Page<ResponseSearchStatResponseDefineDto> search(String search, Pageable page);
}
