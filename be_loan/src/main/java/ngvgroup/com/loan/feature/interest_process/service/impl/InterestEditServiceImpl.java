package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_process.mapper.LnmTxnIntRateMapper;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRate;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateTier;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRate;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRateTier;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateFixedRepository;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateRepository;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateTierRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateFixedRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateTierRepository;
import ngvgroup.com.loan.feature.interest_process.service.InterestEditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestEditServiceImpl implements InterestEditService {

    private final LnmCfgIntRateRepository lnmCfgIntRateRepository;
    private final LnmTxnIntRateRepository lnmTxnIntRateRepository;
    private final LnmTxnIntRateFixedRepository lnmTxnIntRateFixedRepository;
    private final LnmTxnIntRateTierRepository lnmTxnIntRateTierRepository;
    private final LnmCfgIntRateFixedRepository lnmCfgIntRateFixedRepository;
    private final LnmCfgIntRateTierRepository lnmCfgIntRateTierRepository;
    private final LnmTxnIntRateMapper lnmTxnIntRateMapper;

    @Override
    @Transactional
    public void endProcess(String processInstanceCode) {
        LnmTxnIntRate txnIntRate = lnmTxnIntRateRepository.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        LnmCfgIntRate cfgIntRate = lnmCfgIntRateRepository.findByInterestRateCode(txnIntRate.getInterestRateCode()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        LnmCfgIntRate map = lnmTxnIntRateMapper.toCfg(txnIntRate);
        map.setId(cfgIntRate.getId());
        lnmCfgIntRateRepository.save(map);

        List<LnmCfgIntRateFixed> rateFixedList = new ArrayList<>();
        List<LnmCfgIntRateTier> rateTierList = new ArrayList<>();

        if (map.getApplyType().equalsIgnoreCase(LoanVariableConstants.FIXED)) {
            lnmCfgIntRateFixedRepository.deleteAllByInterestRateCode(map.getInterestRateCode());
            lnmCfgIntRateTierRepository.deleteAllByInterestRateCode(map.getInterestRateCode());
            List<LnmTxnIntRateFixed> txnIntRateFixedList = lnmTxnIntRateFixedRepository.findAllByProcessInstanceCode(processInstanceCode);
            txnIntRateFixedList.forEach(fixed -> {
                LnmCfgIntRateFixed cfgIntRateFixed = lnmTxnIntRateMapper.toFixedCfg(fixed);
                rateFixedList.add(cfgIntRateFixed);
            });
            lnmCfgIntRateFixedRepository.saveAll(rateFixedList);
        } else if (map.getApplyType().equalsIgnoreCase(LoanVariableConstants.TIER)) {
            lnmCfgIntRateFixedRepository.deleteAllByInterestRateCode(map.getInterestRateCode());
            lnmCfgIntRateTierRepository.deleteAllByInterestRateCode(map.getInterestRateCode());
            List<LnmTxnIntRateTier> txnIntRateTierList = lnmTxnIntRateTierRepository.findAllByProcessInstanceCode(processInstanceCode);
            txnIntRateTierList.forEach(tier -> {
                LnmCfgIntRateTier cfgIntRateTier = lnmTxnIntRateMapper.toTierCfg(tier);
                rateTierList.add(cfgIntRateTier);
            });
            lnmCfgIntRateTierRepository.saveAll(rateTierList);
        } else {
            throw new BusinessException(ErrorCode.NOT_FOUND);
        }


    }
}
