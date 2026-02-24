package ngvgroup.com.rpt.features.dim.service.impl;

import lombok.RequiredArgsConstructor;
import ngvgroup.com.rpt.features.dim.dto.DimCiBrDDTO;
import ngvgroup.com.rpt.features.dim.repository.DimCiBrDRepository;
import ngvgroup.com.rpt.features.dim.service.DimCiBrDService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DimCiBrDServiceImpl implements DimCiBrDService {
    private final DimCiBrDRepository repo;
    @Override
    public Page<DimCiBrDDTO> pageDimCiBr(String keyword, String scoreInstanceCode, Pageable pageable) {
        BigDecimal keywordNumeric = null;
        try {
            keywordNumeric = new BigDecimal(keyword);
        } catch (NumberFormatException ignored) {
            // Non-numeric keyword — keywordNumeric stays null
        }
        return repo.pageDimCiBr(keyword,scoreInstanceCode, keywordNumeric, pageable);
    }
}
