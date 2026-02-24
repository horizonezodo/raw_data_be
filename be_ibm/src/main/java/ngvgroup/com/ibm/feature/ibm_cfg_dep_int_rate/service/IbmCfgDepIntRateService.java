package ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateDTO;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.dto.IbmCfgDepIntRateSearch;
import ngvgroup.com.ibm.feature.ibm_cfg_dep_int_rate.model.IbmCfgDepIntRate;

import java.util.List;

public interface IbmCfgDepIntRateService extends BaseService<IbmCfgDepIntRate, IbmCfgDepIntRateDTO>  {

    List<IbmCfgDepIntRateSearch> searchCfgDepIntRate();
    List<IbmCfgDepIntRateSearch> exportExcel();
}
