package ngvgroup.com.fac.feature.ctg_cfg_acc_class.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.mapper.FacCfgAccClassMapper;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClass;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.repository.FacCfgAccClassRepository;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.service.FacCfgAccClassService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;


@Service

public class FacCfgAccClassServiceImpl extends BaseServiceImpl<FacCfgAccClass, FacCfgAccClassDto> implements FacCfgAccClassService {

    private final FacCfgAccClassRepository ctgCfgAccClassRepository;
    private final ExportExcel exportExcel;
    public FacCfgAccClassServiceImpl(
            FacCfgAccClassRepository ctgCfgAccClassRepository,
            ExportExcel exportExcel,
            FacCfgAccClassMapper ctgCfgAccClassMapper
    ) {
        super(ctgCfgAccClassRepository,ctgCfgAccClassMapper);
        this.ctgCfgAccClassRepository = ctgCfgAccClassRepository;
        this.exportExcel = exportExcel;
    }
    @Override
    public Page<FacCfgAccClassDto> searchAll(String keyword, Pageable pageable){
        return ctgCfgAccClassRepository.searchAll(keyword,pageable);
    }

    @Override
    public ResponseEntity<byte[]> exportToExcel(String keyword,String fileName){
       try {
           List<FacCfgAccClassDto> ctgCfgAccClasses = ctgCfgAccClassRepository.exportToExcel(keyword);
           String safeFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());
           return exportExcel.exportExcel(ctgCfgAccClasses,safeFileName);
       }
       catch (Exception e){
           throw new BusinessException(FacErrorCode.WRITE_EXCEL_ERROR, e);
       }

    }

    @Override
    public boolean existsByAccClassCode(String accClassCode){
        return ctgCfgAccClassRepository.existsByAccClassCode(accClassCode);
    }

    @Override
    public List<FacCfgAccClassDto> findAllByAccSideType(String accSideType) {
        return ctgCfgAccClassRepository.findAllByAccSideType(accSideType);
    }
}
