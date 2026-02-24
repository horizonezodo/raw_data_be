package ngvgroup.com.fac.feature.fac_inf_acc.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccBalHistoryDto;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;

import java.time.LocalDate;
import java.util.List;

public interface FacInfAccBalService extends BaseService<FacInfAccBal, FacInfAccBalHistoryDto> {
    List<FacInfAccBalHistoryDto> getHistory(String accNo, LocalDate fromDate, LocalDate toDate);
}
