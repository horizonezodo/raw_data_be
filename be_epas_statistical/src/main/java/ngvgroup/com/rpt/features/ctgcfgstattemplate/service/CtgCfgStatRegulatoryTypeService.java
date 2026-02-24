package ngvgroup.com.rpt.features.ctgcfgstattemplate.service;

import ngvgroup.com.rpt.features.ctgcfgstattemplate.dto.ctgcfgstatregulatorytype.CtgCfgStatRegulatoryTypeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CtgCfgStatRegulatoryTypeService {
    List<CtgCfgStatRegulatoryTypeDTO> getAllData();
    Page<CtgCfgStatRegulatoryTypeDTO> pageRegulatory(String keyword, Pageable pageable);
    List<CtgCfgStatRegulatoryTypeDTO> exportExcel(String search);
    void createRegulatory(CtgCfgStatRegulatoryTypeDTO dto);
    void updateRegulatory(CtgCfgStatRegulatoryTypeDTO dto, Long id);
    void deleteRegulatory(Long id);
    CtgCfgStatRegulatoryTypeDTO getDetail(Long id);
}
