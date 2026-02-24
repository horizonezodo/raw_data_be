package ngvgroup.com.loan.feature.interest_process.service.impl;

import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.interest_process.dto.*;
import ngvgroup.com.loan.feature.interest_process.helper.SyncHelper;
import ngvgroup.com.loan.feature.interest_process.mapper.LnmTxnIntRateMapper;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRate;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateFixed;
import ngvgroup.com.loan.feature.interest_process.model.cfg.LnmCfgIntRateTier;
import ngvgroup.com.loan.feature.interest_process.model.txn.LnmTxnIntRate;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateFixedRepository;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateRepository;
import ngvgroup.com.loan.feature.interest_process.repository.cfg.LnmCfgIntRateTierRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateFixedRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateRepository;
import ngvgroup.com.loan.feature.interest_process.repository.txn.LnmTxnIntRateTierRepository;
import ngvgroup.com.loan.feature.interest_process.service.InterestInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InterestInfoServiceImpl implements InterestInfoService {
    private final LnmCfgIntRateRepository lnmCfgIntRateRepository;
    private final LnmTxnIntRateRepository txnRepo;
    private final LnmTxnIntRateFixedRepository lnmTxnIntRateFixedRepository;
    private final LnmTxnIntRateTierRepository lnmTxnIntRateTierRepository;
    private final LnmCfgIntRateFixedRepository lnmCfgIntRateFixedRepository;
    private final LnmCfgIntRateTierRepository lnmCfgIntRateTierRepository;
    private final SyncHelper syncHelper;
    private final LnmTxnIntRateMapper lnmTxnIntRateMapper;
    private final ExportExcel exportExcel;
    private final BpmFeignClient bpmFeignClient;

    @Override
    public Page<LnmCfgIntRateDTO> search(String keyword, List<String> commonCodes, Pageable pageable) {
        return lnmCfgIntRateRepository.search(keyword, commonCodes, pageable);
    }

    @Override
    public InterestProfileDTO getInterestDetail(String processInstanceCode) {
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
    public List<String> getAllInterestRateCode() {
        List<String> rateCodeList = new ArrayList<>();
        txnRepo.findAll().forEach(rate -> rateCodeList.add(rate.getInterestRateCode()));
        return rateCodeList;
    }

    @Override
    public InterestDetailDTO getInterestDetail(Long id) {
        LnmCfgIntRate cfg = lnmCfgIntRateRepository
                .findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        InterestProfileDTO dto = new InterestProfileDTO();
        dto.setOrgCode(cfg.getOrgCode());
        dto.setCurrencyCode(cfg.getCurrencyCode());
        dto.setInterestRateCode(cfg.getInterestRateCode());
        dto.setInterestRateName(cfg.getInterestRateName());
        dto.setInterestRateType(cfg.getInterestRateType());
        dto.setApplyType(cfg.getApplyType());

        if (LoanVariableConstants.FIXED.equals(cfg.getApplyType())) {
            dto.setHistory(buildFixedHistoryByCode(cfg.getInterestRateCode()));
        } else if (LoanVariableConstants.TIER.equals(cfg.getApplyType())) {
            dto.setHistory(buildTierHistoryByCode(cfg.getInterestRateCode()));
        } else {
            dto.setHistory(List.of());
        }
        List<ProcessFileDto> files = bpmFeignClient
                .getProcessFilesFromReferenceCode(cfg.getInterestRateCode(), LoanVariableConstants.PREFIX_INTEREST_REGISTER).getData();

        return new InterestDetailDTO(dto,files);
    }

    @Override
    @Transactional
    public void deleteInterest(Long id) {
        LnmCfgIntRate cfgIntRate =
                lnmCfgIntRateRepository.findById(id)
                        .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        List<LnmTxnIntRate> txnIntRates = txnRepo.findAllByInterestRateCode(cfgIntRate.getInterestRateCode());
        txnIntRates.forEach(rate -> rate.setIsDelete(1));
        txnRepo.saveAll(txnIntRates);

        if (LoanVariableConstants.FIXED.equals(cfgIntRate.getApplyType())) {

            lnmTxnIntRateFixedRepository
                    .softDeleteByInterestRateCode(cfgIntRate.getInterestRateCode());
            lnmCfgIntRateFixedRepository
                    .softDeleteByInterestRateCode(cfgIntRate.getInterestRateCode());
        }
        if (LoanVariableConstants.TIER.equals(cfgIntRate.getApplyType())) {
            lnmTxnIntRateTierRepository
                    .softDeleteByInterestRateCode(cfgIntRate.getInterestRateCode());
            lnmCfgIntRateTierRepository
                    .softDeleteByInterestRateCode(cfgIntRate.getInterestRateCode());
        }
        cfgIntRate.setIsDelete(1);
        lnmCfgIntRateRepository.save(cfgIntRate);
    }
    private List<InterestHistoryDTO> buildFixedHistoryByCode(String interestRateCode) {

        List<LnmCfgIntRateFixed> list =
                lnmCfgIntRateFixedRepository
                        .findAllByInterestRateCode(interestRateCode);

        return syncHelper.buildHistory(
                list,
                e -> new HistoryKey(
                        e.getDecisionNo(),
                        e.getDecisionDate(),
                        e.getEffectiveDate()
                ),
                syncHelper::mapCfgFixedToDetail
        );
    }

    private List<InterestHistoryDTO> buildTierHistoryByCode(String interestRateCode) {

        List<LnmCfgIntRateTier> list =
                lnmCfgIntRateTierRepository
                        .findAllByInterestRateCode(interestRateCode);

        return syncHelper.buildHistory(
                list,
                e -> new HistoryKey(
                        e.getDecisionNo(),
                        e.getDecisionDate(),
                        e.getEffectiveDate()
                ),
                syncHelper::mapCfgTierToDetail
        );
    }


    @Override
    public List<CfgInterestRateDTO> getListRate() {
        List<LnmCfgIntRate> list = lnmCfgIntRateRepository.findAll();
        List<CfgInterestRateDTO> listDTO = new ArrayList<>();

        list.forEach(
                rate -> listDTO.add(lnmTxnIntRateMapper.toCfgDTO(rate))

        );

        return listDTO;
    }



    @Override
    public ResponseEntity<byte[]> exportExcel() {
        try {
            return exportExcel.exportExcel(this.lnmCfgIntRateRepository.exportExcelData(), "Danh_sach_lai_suat");
        } catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR);
        }
    }

}
