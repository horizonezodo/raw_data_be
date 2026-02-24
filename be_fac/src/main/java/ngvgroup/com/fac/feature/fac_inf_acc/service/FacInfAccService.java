package ngvgroup.com.fac.feature.fac_inf_acc.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.*;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FacInfAccService extends BaseService<FacInfAcc, FacInfAccDetailDto> {
    Page<FacInfAccDto> search(List<String> accScope, Pageable pageable);

    void createFacInfAcc(FacInfAccRequest facInfAccRequest);

    void updateFacInfAcc(FacInfAccRequest facInfAccRequest);

    String generateAccountNo(GenerateAccountNoRequest req);

    List<FacInfAccDto> getByOrgCode( String orgCode);

    void deleteFacInfAcc(String accNo);

    List<FacInfAccDto> exportExcel();

    List<String> getAccNo(String accClassCode, String orgCode);

    FacInfAccDtoRes getAllByAccNo(String accNo);

    List<String> getAccClassCodes();

    List<FacInfAccDto> getAccountsByClass(String accClassCode, String orgCode);
}
