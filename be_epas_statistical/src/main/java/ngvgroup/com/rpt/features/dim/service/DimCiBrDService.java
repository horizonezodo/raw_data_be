package ngvgroup.com.rpt.features.dim.service;

import ngvgroup.com.rpt.features.dim.dto.DimCiBrDDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DimCiBrDService {
    Page<DimCiBrDDTO> pageDimCiBr(String keyword,String scoreInstanceCode, Pageable pageable);
}
