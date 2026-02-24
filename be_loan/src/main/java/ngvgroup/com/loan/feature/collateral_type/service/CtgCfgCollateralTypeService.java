package ngvgroup.com.loan.feature.collateral_type.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeDto;
import ngvgroup.com.loan.feature.collateral_type.dto.CtgCfgCollateralTypeResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CtgCfgCollateralTypeService {
    CtgCfgCollateralTypeDto findOne(Long id);
    void create(CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto);
    void update(Long id, CtgCfgCollateralTypeDto ctgCfgCollateralTypeDto);
    void delete(Long id);
    Page<CtgCfgCollateralTypeResponse> searchAll(String filter, Pageable pageable, boolean isExport);
    ResponseEntity<byte[]> exportToExcel(String filter, List<String> labels);
    boolean checkExist(String code);


}
