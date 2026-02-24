package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.core.utils.GenerateNextSequence;
import ngvgroup.com.loan.feature.interest_process.helper.SyncHelper;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRate;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateFixedRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateTierRepository;
import ngvgroup.com.loan.feature.interest_process.service.InterestEditService;
import ngvgroup.com.loan.feature.interest_process.service.InterestRegisterService;
import ngvgroup.com.loan.feature.interest_process.dto.InterestProfileDTO;
import ngvgroup.com.loan.feature.interest_process.mapper.LnmTxnIntRateMapper;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRate;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateRepository;
import ngvgroup.com.loan.feature.interest_process.service.InterestTransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InterestTransactionServiceImpl implements InterestTransactionService {
    private final LnmTxnIntRateRepository txnRepo;
    private final LnmTxnIntRateMapper mapper;
    private final SyncHelper syncHelper;
    private final InterestRegisterService interestRegisterService;
    private final InterestEditService interestEditService;
    private final LnmTxnIntRateFixedRepository lnmTxnIntRateFixedRepository;
    private final LnmTxnIntRateTierRepository lnmTxnIntRateTierRepository;
    private final LnmCfgIntRateRepository lnmCfgIntRateRepository;

    @Override
    public void createInterest(InterestProfileDTO profile, Long id) {

        boolean isEditProcess = id != null;

        saveTxnRate(profile, profile.getProcessInstanceCode(), id);

        handleHistoryByProcessType(profile, isEditProcess);
    }

    private void saveTxnRate(
            InterestProfileDTO profile,
            String processInstanceCode,
            Long id
    ) {
        LnmTxnIntRate entity;
        entity = mapper.toEntity(profile);
        if (id != null) {
            entity.setId(id);
            entity.setProcessTypeCode(LoanVariableConstants.INTEREST_EDIT_PROCESS_TYPE_CODE);
        } else {
            entity.setProcessTypeCode(LoanVariableConstants.INTEREST_REGISTER_PROCESS_TYPE_CODE);
        }
        entity.setProcessInstanceCode(processInstanceCode);

        txnRepo.save(entity);
    }

    private void handleHistoryByProcessType(InterestProfileDTO profile, boolean isEditProcess) {

        String newType = profile.getApplyType();

        if (!isEditProcess) {
            saveByType(profile, profile.getInterestRateCode(), profile.getProcessInstanceCode(), newType);
            return;
        }

        LnmCfgIntRate cfg = lnmCfgIntRateRepository.findByInterestRateCode(profile.getInterestRateCode()).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        String oldType = cfg.getApplyType();

        if (oldType == null || oldType.equals(newType)) {
            updateByType(profile);
        } else {
            deleteByType(oldType, profile.getInterestRateCode());
            saveByType(profile, profile.getInterestRateCode(), profile.getProcessInstanceCode(), newType);
        }
    }

    private void updateByType(InterestProfileDTO profile) {
        if (LoanVariableConstants.FIXED.equals(profile.getApplyType())) {
            syncHelper.updateFixed(profile);
        } else {
            syncHelper.updateTier(profile);
        }
    }

    private void saveByType(InterestProfileDTO profile, String rateCode, String processCode, String type) {
        if (LoanVariableConstants.FIXED.equals(type)) {
            syncHelper.saveFixedHistory(profile, rateCode, processCode);
        } else {
            syncHelper.saveTierHistory(profile, rateCode, processCode);
        }
    }

    private void deleteByType(String type, String rateCode) {
        if (LoanVariableConstants.FIXED.equals(type)) {
            lnmTxnIntRateFixedRepository.deleteAllByInterestRateCode(rateCode);
        } else {
            lnmTxnIntRateTierRepository.deleteAllByInterestRateCode(rateCode);
        }
    }

    private void handleHistoryTransition(
            InterestProfileDTO profile,
            String oldType,
            String newType,
            String rateCode,
            String processCode
    ) {

        if (oldType == null) {
            saveByType(profile, rateCode, processCode, newType);
            return;
        }

        if (oldType.equals(newType)) {
            updateByType(profile);
            return;
        }

        deleteByType(oldType, rateCode);
        saveByType(profile, rateCode, processCode, newType);
    }

    @Override
    public void updateInterest(InterestProfileDTO profile) {

        LnmTxnIntRate txn = txnRepo.findByProcessInstanceCode(
                profile.getProcessInstanceCode()
        ).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String oldType = txn.getApplyType();
        String newType = profile.getApplyType();

        mapper.updateTxn(profile, txn);

        handleHistoryTransition(
                profile,
                oldType,
                newType,
                profile.getInterestRateCode(),
                profile.getProcessInstanceCode()
        );

        txnRepo.save(txn);
    }

    @Override
    public void updateInterest(String processInstanceCode, InterestProfileDTO profile) {
        //
    }

    @Override
    public InterestProfileDTO getDetail(String processInstanceCode) {
        LnmTxnIntRate txn = txnRepo
                .findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        InterestProfileDTO dto = new InterestProfileDTO();
        dto.setOrgCode(txn.getOrgCode());
        dto.setCurrencyCode(txn.getCurrencyCode());
        dto.setProcessInstanceCode(txn.getProcessInstanceCode());
        dto.setInterestRateCode(txn.getInterestRateCode());
        dto.setInterestRateName(txn.getInterestRateName());
        dto.setInterestRateType(txn.getInterestRateType());
        dto.setApplyType(txn.getApplyType());

        if (LoanVariableConstants.FIXED.equals(txn.getApplyType())) {
            dto.setHistory(syncHelper.buildFixedHistory(processInstanceCode));
        } else if (LoanVariableConstants.TIER.equals(txn.getApplyType())) {
            dto.setHistory(syncHelper.buildTierHistory(processInstanceCode));
        } else {
            dto.setHistory(List.of());
        }
        return dto;
    }

    @Override
    public void cancelRequest(String processInstanceCode) {
        LnmTxnIntRate rate = txnRepo.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        rate.setBusinessStatus(VariableConstants.CANCEL);
        txnRepo.save(rate);
    }

    @Override
    public void updateEndProcess(String processInstanceCode, String type) {
        if (type.equalsIgnoreCase(LoanVariableConstants.PROCESS_REGISTER_TYPE)) {
            interestRegisterService.endProcess(processInstanceCode);
        } else if (type.equalsIgnoreCase(LoanVariableConstants.PROCESS_EDIT_TYPE)) {
            interestEditService.endProcess(processInstanceCode);
        }
    }

    @Getter
    private final GenerateNextSequence sequence;
}
