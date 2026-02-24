package ngvgroup.com.fac.feature.single_entry_acct.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.component.GenerateCodeService;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.dto.FacCfgAccClassCoaMapResDto;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.repository.FacCfgAccClassCoaMapRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccA;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBalA;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalARepository;
import ngvgroup.com.fac.feature.single_entry_acct.dto.*;
import ngvgroup.com.fac.feature.single_entry_acct.mapper.AccountEntryMapper;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryDtlRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctRepository;
import ngvgroup.com.fac.feature.single_entry_acct.service.SingleEntryAcctService;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SingleEntryAcctServiceImpl implements SingleEntryAcctService {

    private final FacTxnAcctRepository facTxnAcctRepo;
    private final FacTxnAcctEntryRepository facTxnAcctEntryRepo;
    private final GenerateCodeService generateCodeService;
    private final FacTxnAcctEntryDtlRepository entryDtlRepo;
    private final AccountEntryMapper accountEntryMapper;
    private final FacInfAccRepository facInfAccRepository;
    private final FacInfAccBalRepository facInfAccBalRepository;
    private final FacCfgAccClassCoaMapRepository accClassCoaMapRepo;
    private final FacInfAccARepository infAccARepo;
    private final FacInfAccBalARepository infAccBalARepo;

    // Create single entry accounting
    @Override
    @Transactional
    public void createAccountEntry(SingleEntryAcctDTO dto, String processInstanceCode) {
        saveToTxnEntity(dto, processInstanceCode);
    }

    private void saveToTxnEntity(SingleEntryAcctDTO dto, String processInstanceCode) {

        TxnAcctEntryDTOForm entryDTOForm = dto.getTransactionInfo().getAcctEntryForm();
        TxnAcctDTOForm acctDTOForm = dto.getTransactionInfo().getAcctForm();
        List<AccountingInfoDTO> accountingInfoDTO = dto.getAccountingInfo();

        // 1. Sinh mã
        String acctEntryCode = generateCodeService.generateCode(entryDTOForm.getCommon().getOrgCode(), processInstanceCode, FacVariableConstants.FAC_TXN_ACCT_ENTRY_TABLE, FacVariableConstants.TXN_ACCT_ENTRY_CODE, 1, 3, ".");

        int lastDot = acctEntryCode.lastIndexOf('.');
        int secondLastDot = acctEntryCode.lastIndexOf('.', lastDot - 1);

        String resultAcctEntryCode = acctEntryCode.substring(0, secondLastDot) + acctEntryCode.substring(lastDot);

        acctDTOForm.setTotalForeignAmt(entryDTOForm.getEntryForeignAmt());

        saveFactTxnAcctEntry(entryDTOForm, processInstanceCode, resultAcctEntryCode);
        saveFactTxnAcct(acctDTOForm, processInstanceCode);
        saveFactTxnAcctEntryDtl(accountingInfoDTO, processInstanceCode, resultAcctEntryCode, entryDTOForm);
    }

    // Save to TXN entity
    private void saveFactTxnAcctEntry(TxnAcctEntryDTOForm entryDTOForm, String processInstanceCode, String acctEntryCode) {
        String orgCode = entryDTOForm.getCommon().getOrgCode();
        LocalDate txnDate = entryDTOForm.getCommon().getTxnDate();
        BigDecimal entryForeignAmt = entryDTOForm.getEntryForeignAmt();

        String voucherNo = generateCodeService.generateVoucherCode(entryDTOForm.getCommon().getOrgCode(), entryDTOForm.getVoucherTypeCode());

        FacTxnAcctEntry entry = accountEntryMapper.toFacCfgAcctEntry(entryDTOForm);
        entry.setOrgCode(orgCode);
        entry.setTxnDate(txnDate);
        entry.setProcessInstanceCode(processInstanceCode);
        entry.setVoucherNo(voucherNo);
        entry.setTxnAcctEntryCode(acctEntryCode);
        entry.setEntryBaseAmt(entryForeignAmt);
        entry.setCurrencyCode(FacVariableConstants.CURRENCY_VND);
        entry.setBusinessStatus(FacVariableConstants.ACTIVE);

        facTxnAcctEntryRepo.save(entry);
    }

    private void saveFactTxnAcct(TxnAcctDTOForm acctDTOForm, String processInstanceCode) {
        String orgCode = acctDTOForm.getCommon().getOrgCode();
        LocalDate txnDate = acctDTOForm.getCommon().getTxnDate();
        BigDecimal totalForeignAmt = acctDTOForm.getTotalForeignAmt();

        FacTxnAcct entity = accountEntryMapper.toFacCfgAcct(acctDTOForm);
        entity.setOrgCode(orgCode);
        entity.setTxnDate(txnDate);
        entity.setProcessInstanceCode(processInstanceCode);
        entity.setTotalBaseAmt(totalForeignAmt);
        entity.setProcessTypeCode(FacVariableConstants.FAC_PROCESS_TYPE_CODE);
        entity.setCurrencyCode(FacVariableConstants.CURRENCY_VND);
        entity.setBusinessStatus(FacVariableConstants.ACTIVE);

        facTxnAcctRepo.save(entity);
    }

    private void saveFactTxnAcctEntryDtl(List<AccountingInfoDTO> accountingInfoDTO, String processInstanceCode, String resultAcctEntryCode, TxnAcctEntryDTOForm entryDTOForm) {

        List<FacTxnAcctEntryDtl> dtlResult = new ArrayList<>();
        BigDecimal entryForeignAmt = entryDTOForm.getEntryForeignAmt();
        String orgCode = entryDTOForm.getCommon().getOrgCode();
        LocalDate txnDate = entryDTOForm.getCommon().getTxnDate();

        for (AccountingInfoDTO accountingInfo : accountingInfoDTO) {
            FacTxnAcctEntryDtl dtl = accountEntryMapper.toFacTxnAcctEntryDtl(accountingInfo);

            FacCfgAccClassCoaMapResDto classCoaMapResDto = accClassCoaMapRepo.findByOrgCodeAndAccClassCode(orgCode, dtl.getAccClassCode()).orElseThrow(() -> new BusinessException(FacErrorCode.ACC_CLASS_COA_MAP_NOT_EXIST));

            String acctEntryDtlCode = generateCodeService.generateCode(orgCode, resultAcctEntryCode, FacVariableConstants.FAC_TXN_ACCT_ENTRY_DTL, FacVariableConstants.TXN_ACCT_ENTRY_DTL_CODE, 1, 4, ".");

            dtl.setOrgCode(orgCode);
            dtl.setLineBaseAmt(entryForeignAmt);
            dtl.setTxnDate(txnDate);
            dtl.setProcessInstanceCode(processInstanceCode);
            dtl.setBusinessStatus(FacVariableConstants.ACTIVE);
            dtl.setCurrencyCode(FacVariableConstants.CURRENCY_VND);
            dtl.setTxnAcctEntryDtlCode(acctEntryDtlCode);
            dtl.setTxnAcctEntryCode(resultAcctEntryCode);
            dtl.setAccCoaCode(classCoaMapResDto.getAccCoaCode());
            dtl.setLineForeignAmt(BigDecimal.valueOf(0));
            dtlResult.add(dtl);
        }

        entryDtlRepo.saveAll(dtlResult);
    }

    @Override
    @Transactional
    public SingleEntryAcctDTO getDetail(String processInstanceCode) {

        List<SingleEntryAcctResDetailDTO> resDetailDTO = facTxnAcctRepo.getListByProcessInstanceCode(processInstanceCode);

        //Mapping DTO
        TransactionInfoDTO transactionInfoDTO = new TransactionInfoDTO();

        List<AccountingInfoDTO> accountingInfoDTOList = new ArrayList<>();
        for (SingleEntryAcctResDetailDTO resDetail : resDetailDTO) {

            //Set common value
            CommonTransactionInfoDTO common = CommonTransactionInfoDTO.builder()
                    .orgCode(resDetail.getOrgCode())
                    .txnDate(resDetail.getTxnDate())
                    .processInstanceCode(processInstanceCode)
                    .build();
            // Set form1 value
            TxnAcctEntryDTOForm entryDTOForm = TxnAcctEntryDTOForm.builder()
                    .common(common)
                    .voucherTypeCode(resDetail.getVoucherTypeCode())
                    .entryTypeCode(resDetail.getEntryTypeCode())
                    .entryForeignAmt(resDetail.getEntryForeignAmt())
                    .build();

            // set form2 value
            TxnAcctDTOForm acctDTOForm = TxnAcctDTOForm.builder()
                    .common(common)
                    .totalForeignAmt(resDetail.getTotalForeignAmt())
                    .txnContent(resDetail.getTxnContent())
                    .txnTime(resDetail.getTxnTime())
                    .objectTypeCode(resDetail.getObjectTypeCode())
                    .objectTxnCode(resDetail.getObjectTxnCode())
                    .objectTxnName(resDetail.getObjectTxnName())
                    .identificationId(resDetail.getIdentificationId())
                    .issueDate(resDetail.getIssueDate())
                    .issuePlace(resDetail.getIssuePlace())
                    .address(resDetail.getAddress())
                    .build();

            transactionInfoDTO.setAcctEntryForm(entryDTOForm);
            transactionInfoDTO.setAcctForm(acctDTOForm);

            accountingInfoDTOList.add(AccountingInfoDTO.builder()
                    .idDtl(resDetail.getIdDtl())
                    .accClassCode(resDetail.getAccClassCode())
                    .entryType(resDetail.getEntryType())
                    .accNo(resDetail.getAccNo())
                    .lineForeignAmt(resDetail.getLineForeignAmt())
                    .orgCode(resDetail.getOrgCode())
                    .accCoaCode(resDetail.getAccCoaCode())
                    .txnDate(resDetail.getTxnDate())
                    .lineBaseAmt(resDetail.getLineBaseAmt())
                    .balAvailable(resDetail.getBalAvailable())
                    .balActual(resDetail.getBalActual())
                    .build());
        }

        SingleEntryAcctDTO result = new SingleEntryAcctDTO();
        result.setTransactionInfo(transactionInfoDTO);
        result.setAccountingInfo(accountingInfoDTOList);

        return result;
    }

    @Override
    public void cancelRequest(String processInstanceCode) {
        FacTxnAcctEntry entry = facTxnAcctEntryRepo.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, processInstanceCode));

        FacTxnAcct acct = facTxnAcctRepo.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, processInstanceCode));

        List<FacTxnAcctEntryDtl> dtl = entryDtlRepo.findAllByProcessInstanceCode(processInstanceCode);
        for (FacTxnAcctEntryDtl dtlEntry : dtl) {
            dtlEntry.setBusinessStatus(FacVariableConstants.CANCEL);
            entryDtlRepo.save(dtlEntry);
        }

        entry.setBusinessStatus(FacVariableConstants.CANCEL);
        acct.setBusinessStatus(FacVariableConstants.CANCEL);
        facTxnAcctEntryRepo.save(entry);
        facTxnAcctRepo.save(acct);
    }

    @Transactional
    @Override
    public void updateAccountEntry(SingleEntryAcctDTO dto, String processInstanceCode) {
        TxnAcctEntryDTOForm entryDTOForm = dto.getTransactionInfo().getAcctEntryForm();
        TxnAcctDTOForm acctDTOForm = dto.getTransactionInfo().getAcctForm();
        List<AccountingInfoDTO> accountingInfoDTO = dto.getAccountingInfo();

        updateFactTxnAcctEntry(entryDTOForm, processInstanceCode);
        updateFactTxnAcct(acctDTOForm, processInstanceCode);

        for (AccountingInfoDTO accountingInfo : accountingInfoDTO) {
            updateFactTxnAcctEntryDtl(accountingInfo);
        }
    }

    @Override
    @Transactional
    public void updateEndProcess(String processInstanceCode) {
        if (!StringUtils.hasText(processInstanceCode)) return;

        FacTxnAcctEntry entry = facTxnAcctEntryRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.TXN_ACCT_ENTRY_NOT_EXIST));

        FacTxnAcct acct = facTxnAcctRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.TXN_ACCT_NOT_EXIST));

        List<FacTxnAcctEntryDtl> dtls = entryDtlRepo.findAllByProcessInstanceCode(processInstanceCode);
        if (dtls.isEmpty()) {
            throw new BusinessException(FacErrorCode.TXN_ACCT_ENTRY_NOT_EXIST);
        }

        LocalDateTime now = LocalDateTime.now();

        entry.setBusinessStatus(FacVariableConstants.COMPLETE);
        facTxnAcctEntryRepo.save(entry);

        acct.setBusinessStatus(FacVariableConstants.COMPLETE);
        facTxnAcctRepo.save(acct);

        for (FacTxnAcctEntryDtl dtl : dtls) {
            dtl.setBusinessStatus(FacVariableConstants.COMPLETE);
            entryDtlRepo.save(dtl);
            updateFactInfAcc(dtl, now);
        }
    }


    private void updateFactInfAcc(FacTxnAcctEntryDtl dtl, LocalDateTime now) {
        String accNo = dtl.getAccNo();

        FacInfAcc facInfAcc = facInfAccRepository.findByAccNo(accNo)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND));

        FacInfAccBal facInfAccBal = facInfAccBalRepository.findByAccNo(dtl.getAccNo())
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND));

        BigDecimal lineForeignAmt = dtl.getLineForeignAmt();
        String entryType = dtl.getEntryType();

        BigDecimal bal = facInfAcc.getBal();
        String accNature = facInfAcc.getAccNature();
        BigDecimal balActual = facInfAcc.getBalActual();

        BigDecimal balResult;
        BigDecimal balActualResult;
        if ((accNature.equals("D") && entryType.equals("C")) || (accNature.equals("C") && entryType.equals("D"))) {
            balResult = bal.subtract(lineForeignAmt);
            balActualResult = balActual.subtract(lineForeignAmt);
        } else {
            balResult = bal.add(lineForeignAmt);
            balActualResult = balActual.add(lineForeignAmt);
        }

        facInfAcc.setBal(balResult);
        facInfAcc.setBalActual(balActualResult);
        facInfAcc.setModifiedDate(Timestamp.valueOf(now));

        facInfAccBal.setBal(bal);
        facInfAccBal.setModifiedDate(Timestamp.valueOf(now));
        facInfAccBal.setBalAvailable(facInfAcc.getBalAvailable());

        BigDecimal toTalDrAmt = facInfAccBal.getTotalDrAmt();
        facInfAccBal.setTotalDrAmt(toTalDrAmt.add(lineForeignAmt));
        BigDecimal totalCrAmt = facInfAccBal.getTotalCrAmt();
        facInfAccBal.setTotalCrAmt(totalCrAmt.add(lineForeignAmt));

        facInfAccRepository.save(facInfAcc);
        facInfAccBalRepository.save(facInfAccBal);

        // insert into FAC_INF_ACC_A table
        infAccARepo.save(FacInfAccA.from(facInfAcc));
        infAccBalARepo.save(FacInfAccBalA.fromBal(facInfAccBal));
    }

    private void updateFactTxnAcctEntryDtl(AccountingInfoDTO dto) {

        FacTxnAcctEntryDtl dtl = entryDtlRepo.findById(dto.getIdDtl())
                .orElseThrow(() -> new BusinessException(FacErrorCode.TXN_ACCT_ENTRY_DTL_NOT_EXIST));
        dtl.setEntryType(dto.getEntryType());
        dtl.setAccClassCode(dto.getAccClassCode());

        entryDtlRepo.save(dtl);
    }

    private void updateFactTxnAcct(TxnAcctDTOForm dto, String processInstanceCode) {
        FacTxnAcct entity = facTxnAcctRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.TXN_ACCT_NOT_EXIST));

        entity.setTxnDate(dto.getCommon().getTxnDate());
        entity.setTxnContent(dto.getTxnContent());
        entity.setOrgCode(dto.getCommon().getOrgCode());
        entity.setTxnTime(dto.getTxnTime());
        entity.setObjectTypeCode(dto.getObjectTypeCode());
        entity.setObjectTxnCode(dto.getObjectTxnCode());
        entity.setObjectTxnName(dto.getObjectTxnName());
        entity.setIdentificationId(dto.getIdentificationId());
        entity.setIssueDate(dto.getIssueDate());
        entity.setIssuePlace(dto.getIssuePlace());
        entity.setAddress(dto.getAddress());

        facTxnAcctRepo.save(entity);
    }

    private void updateFactTxnAcctEntry(TxnAcctEntryDTOForm dto, String processInstanceCode) {
        FacTxnAcctEntry entry = facTxnAcctEntryRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.TXN_ACCT_ENTRY_NOT_EXIST));

        entry.setOrgCode(dto.getCommon().getOrgCode());
        entry.setTxnDate(dto.getCommon().getTxnDate());
        entry.setVoucherNo(dto.getVoucherTypeCode());
        entry.setEntryTypeCode(dto.getEntryTypeCode());
        entry.setEntryForeignAmt(dto.getEntryForeignAmt());

        facTxnAcctEntryRepo.save(entry);
    }

}
