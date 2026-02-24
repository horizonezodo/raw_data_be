package ngvgroup.com.fac.feature.fac_inf_acc.service.impl;

import com.ngvgroup.bpm.core.persistence.service.impl.BaseServiceImpl;
import ngvgroup.com.fac.feature.fac_inf_acc.dto.FacInfAccBalHistoryDto;
import ngvgroup.com.fac.feature.fac_inf_acc.mapper.FacInfAccBalMapper;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBalA;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.service.FacInfAccBalService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FacInfAccBalServiceImpl extends BaseServiceImpl<FacInfAccBal, FacInfAccBalHistoryDto> implements FacInfAccBalService {
    private final FacInfAccBalARepository balARepo;
    private final FacInfAccBalRepository balRepo;

    public FacInfAccBalServiceImpl(FacInfAccBalMapper mapper, FacInfAccBalRepository balRepo, FacInfAccBalARepository balARepo) {
        super(balRepo, mapper);
        this.balARepo = balARepo;
        this.balRepo = balRepo;
    }

    @Override
    public List<FacInfAccBalHistoryDto> getHistory(String accNo, LocalDate fromDate, LocalDate toDate) {
        LocalDate sysDate = LocalDate.now();

        List<FacInfAccBalA> histories =
                balARepo.findBalA(accNo);

        List<FacInfAccBalHistoryDto> result = new ArrayList<>();

        BigDecimal prevClosingBal = BigDecimal.ZERO;
        LocalDate lastDate = null;

        for (FacInfAccBalA h : histories) {

            LocalDate dataDate = h.getDataTime()
                    .toLocalDateTime()
                    .toLocalDate();

            if (dataDate.isBefore(sysDate)) {

                if (dataDate.equals(lastDate)) {
                    prevClosingBal = h.getBal();
                } else {

                    FacInfAccBalHistoryDto dto = new FacInfAccBalHistoryDto();
                    dto.setDataDate(dataDate);
                    dto.setOpeningBal(prevClosingBal);
                    dto.setTotalDrAmt(h.getTotalDrAmt());
                    dto.setTotalCrAmt(h.getTotalCrAmt());
                    dto.setClosingBal(h.getBal());

                    prevClosingBal = h.getBal();
                    lastDate = dataDate;

                    result.add(dto);
                }
            }
        }

        BigDecimal finalPrevClosingBal = prevClosingBal;
        balRepo.findByAccNo(accNo).ifPresent(current -> {
            FacInfAccBalHistoryDto dto = new FacInfAccBalHistoryDto();
            dto.setDataDate(sysDate);
            dto.setOpeningBal(finalPrevClosingBal);
            dto.setTotalDrAmt(current.getTotalDrAmt());
            dto.setTotalCrAmt(current.getTotalCrAmt());
            dto.setClosingBal(current.getBal());
            result.add(dto);
        });

        List<FacInfAccBalHistoryDto> filtered = result.stream()
                .filter(r ->
                        (fromDate == null || !r.getDataDate().isBefore(fromDate)) &&
                                (toDate == null || !r.getDataDate().isAfter(toDate))
                )
                .toList();

        for (int i = 0; i < filtered.size(); i++) {
            filtered.get(i).setOrderNo(i + 1);
        }

        return filtered;

    }
}
