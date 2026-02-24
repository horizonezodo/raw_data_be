package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_process.helper.SyncHelper;
import ngvgroup.com.loan.feature.interest_process.dto.*;
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
import ngvgroup.com.loan.feature.interest_process.service.InterestRegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class InterestRegisterServiceImpl implements InterestRegisterService {

    private final LnmTxnIntRateRepository lnmTxnIntRateRepository;
    private final LnmTxnIntRateFixedRepository lnmTxnIntRateFixedRepository;
    private final LnmTxnIntRateTierRepository lnmTxnIntRateTierRepository;
    private final LnmCfgIntRateRepository lnmCfgIntRateRepository;
    private final LnmCfgIntRateFixedRepository lnmCfgIntRateFixedRepository;
    private final LnmCfgIntRateTierRepository lnmCfgIntRateTierRepository;
    private final LnmTxnIntRateMapper lnmTxnIntRateMapper;
    private final SyncHelper syncHelper;

    public InterestRegisterServiceImpl(LnmTxnIntRateRepository lnmTxnIntRateRepository, LnmTxnIntRateFixedRepository lnmTxnIntRateFixedRepository, LnmTxnIntRateTierRepository lnmTxnIntRateTierRepository, LnmCfgIntRateRepository lnmCfgIntRateRepository, LnmCfgIntRateFixedRepository lnmCfgIntRateFixedRepository, LnmCfgIntRateTierRepository lnmCfgIntRateTierRepository, LnmTxnIntRateMapper lnmTxnIntRateMapper, SyncHelper syncHelper) {
        super();
        this.lnmTxnIntRateRepository = lnmTxnIntRateRepository;
        this.lnmTxnIntRateFixedRepository = lnmTxnIntRateFixedRepository;
        this.lnmTxnIntRateTierRepository = lnmTxnIntRateTierRepository;
        this.lnmCfgIntRateRepository = lnmCfgIntRateRepository;
        this.lnmCfgIntRateFixedRepository = lnmCfgIntRateFixedRepository;
        this.lnmCfgIntRateTierRepository = lnmCfgIntRateTierRepository;
        this.lnmTxnIntRateMapper = lnmTxnIntRateMapper;
        this.syncHelper = syncHelper;
    }

    @Override
    @Transactional
    public void endProcess(String processInstanceCode) {

        // 1. Load TXN
        LnmTxnIntRate rate =
                lnmTxnIntRateRepository
                        .findByProcessInstanceCode(processInstanceCode)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        // 2. Build profile từ TXN
        InterestProfileDTO profile =
                buildProfileFromTxn(rate, processInstanceCode);

        // 3. Upsert CFG ROOT
        LnmCfgIntRate cfg =
                lnmCfgIntRateRepository
                        .findByInterestRateCode(rate.getInterestRateCode())
                        .orElseGet(LnmCfgIntRate::new);

        lnmTxnIntRateMapper.updateCfg(rate, cfg);
        lnmCfgIntRateRepository.save(cfg);

        // 4. Sync CFG CHILD (FIXED / TIER)
        saveOrUpdateCfgEntities(profile, rate.getInterestRateCode());

        // 5. Complete TXN
        rate.setBusinessStatus(VariableConstants.COMPLETE);
        lnmTxnIntRateRepository.save(rate);
    }


    private InterestProfileDTO buildProfileFromTxn(
            LnmTxnIntRate rate,
            String processInstanceCode
    ) {
        InterestProfileDTO profile = new InterestProfileDTO();

        profile.setOrgCode(rate.getOrgCode());
        profile.setInterestRateCode(rate.getInterestRateCode());
        profile.setApplyType(rate.getApplyType());
        profile.setProcessInstanceCode(processInstanceCode);

        if (LoanVariableConstants.FIXED.equals(rate.getApplyType())) {

            List<LnmTxnIntRateFixed> fixedList =
                    lnmTxnIntRateFixedRepository
                            .findAllByProcessInstanceCode(processInstanceCode);

            profile.setHistory(groupFixedHistory(fixedList));

        } else if (LoanVariableConstants.TIER.equals(rate.getApplyType())) {

            List<LnmTxnIntRateTier> tierList =
                    lnmTxnIntRateTierRepository
                            .findAllByProcessInstanceCode(processInstanceCode);

            profile.setHistory(groupTierHistory(tierList));
        }

        return profile;
    }

    private List<InterestHistoryDTO> groupFixedHistory(
            List<LnmTxnIntRateFixed> list
    ) {
        return groupHistory(
                list,
                f -> new HistoryKey(
                        f.getDecisionNo(),
                        f.getDecisionDate(),
                        f.getEffectiveDate()
                ),
                syncHelper::mapFixedToDetail
        );
    }

    private List<InterestHistoryDTO> groupTierHistory(
            List<LnmTxnIntRateTier> list
    ) {
        return groupHistory(
                list,
                f -> new HistoryKey(
                        f.getDecisionNo(),
                        f.getDecisionDate(),
                        f.getEffectiveDate()
                ),
                syncHelper::mapTierToDetail
        );
    }

    private <E> List<InterestHistoryDTO> groupHistory(
            List<E> list,
            Function<E, HistoryKey> keyExtractor,
            Function<E, InterestHistoryDetailDTO> detailMapper
    ) {
        return list.stream()
                .collect(Collectors.groupingBy(keyExtractor))
                .values()
                .stream()
                .map(group -> {
                    E first = group.get(0);
                    HistoryKey key = keyExtractor.apply(first);

                    InterestHistoryDTO h = new InterestHistoryDTO();
                    h.setDecisionNo(key.getDecisionNo());
                    h.setDecisionDate(key.getDecisionDate());
                    h.setEffectiveDate(key.getEffectiveDate());

                    h.setHistoryDetails(
                            group.stream()
                                    .map(detailMapper)
                                    .toList()
                    );
                    return h;
                })
                .toList();
    }
    private void saveOrUpdateCfgEntities(
            InterestProfileDTO profile,
            String interestRateCode
    ) {
        if (LoanVariableConstants.FIXED.equals(profile.getApplyType())) {
            updateCfgFixed(profile, interestRateCode);
        } else if (LoanVariableConstants.TIER.equals(profile.getApplyType())) {
            updateCfgTier(profile, interestRateCode);
        }
    }

    private void updateCfgFixed(
            InterestProfileDTO dto,
            String interestRateCode
    ) {
        List<LnmCfgIntRateFixed> dbList =
                lnmCfgIntRateFixedRepository
                        .findAllByInterestRateCode(interestRateCode);

        List<FlatHistoryDetail> flat = syncHelper.flattenHistory(dto);

        syncHelper.syncEntities(
                dbList,
                flat,
                f -> f.getDetail().getId(),
                (f, e) -> {
                    InterestHistoryDTO h = f.getParent();
                    InterestHistoryDetailDTO d = f.getDetail();

                    e.setOrgCode(dto.getOrgCode());
                    e.setInterestRateCode(interestRateCode);

                    syncHelper.mapFixed(h, d, e);
                },
                LnmCfgIntRateFixed::new,
                lnmCfgIntRateFixedRepository
        );
    }

    private void updateCfgTier(
            InterestProfileDTO dto,
            String interestRateCode
    ) {
        List<LnmCfgIntRateTier> dbList =
                lnmCfgIntRateTierRepository
                        .findAllByInterestRateCode(interestRateCode);

        List<FlatHistoryDetail> flat = syncHelper.flattenHistory(dto);

        syncHelper.syncEntities(
                dbList,
                flat,
                f -> f.getDetail().getId(),
                (f, e) -> {
                    InterestHistoryDTO h = f.getParent();
                    InterestHistoryDetailDTO d = f.getDetail();

                    e.setOrgCode(dto.getOrgCode());
                    e.setInterestRateCode(interestRateCode);

                    syncHelper.mapTier(h, d, e);
                },
                LnmCfgIntRateTier::new,
                lnmCfgIntRateTierRepository
        );
    }
}
