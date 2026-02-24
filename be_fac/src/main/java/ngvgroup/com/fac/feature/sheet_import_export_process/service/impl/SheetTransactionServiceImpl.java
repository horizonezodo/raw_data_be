package ngvgroup.com.fac.feature.sheet_import_export_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.component.GenerateCodeService;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.model.FacCfgAccClassCoaMap;
import ngvgroup.com.fac.feature.ctg_cfg_acc_class.repository.FacCfgAccClassCoaMapRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccA;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBal;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccBalA;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccBalRepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccRepository;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.AccountEntryDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetTransactionService;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryDtlRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SheetTransactionServiceImpl implements SheetTransactionService {
    private final GenerateCodeService generateCodeService;

    private final FacTxnAcctRepository facTxnAcctRepo;
    private final FacTxnAcctEntryRepository facTxnAcctEntryRepo;
    private final FacTxnAcctEntryDtlRepository facTxnAcctEntryDtlRepo;
    private final FacCfgAccClassCoaMapRepository classCoaMapRepo;
    private final FacInfAccBalRepository accBalRepo;
    private final FacInfAccRepository accRepo;
    private final FacInfAccBalARepository accBalARepo;
    private final FacInfAccARepository accARepo;

    private static final String VOUCHER_TYPE_IMPORT = "NNB";
    private static final String VOUCHER_TYPE_EXPORT = "XNB";
    private static final String ENTRY_TYPE_DEBIT = "D";
    private static final String ENTRY_TYPE_CREDIT = "C";
    private static final String DEFAULT_STATUS = "NHAP_CHO_DUYET";
    private static final String BUSINESS_STATUS_ACTIVE = "ACTIVE";
    private static final String PROCESS_TYPE_CODE = "FAC.201.01";
    private static final String ENTRY_TYPE_CODE_CTNB = "CTNB";
    private static final String BUSINESS_STATUS_CANCEL = "cancel";
    private static final String STATUS_COMPLETE = "COMPLETE";

    @Override
    public String generateSeq(String orgCode) {
        return generateCodeService.generateCode(
                orgCode,
                FacVariableConstants.PREFIX_SHEET,
                FacVariableConstants.FAC_TXN_ACCT_ENTRY_TABLE,
                FacVariableConstants.PROCESS_INSTANCE_CODE, 1,
                5, ".");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcess(SheetInfoDto request) {
        var saveAcct = upsertTransaction(new FacTxnAcct(), new FacTxnAcctEntry(), request, request.getProcessInstanceCode());
        var entry = getEntryOrThrow(request.getProcessInstanceCode());
        processBalancesForInit(request, entry, saveAcct);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void executeEditBusiness(SheetInfoDto req, String processInstanceCode) {
        FacTxnAcct existingAcct = getAcctOrThrow(processInstanceCode);
        FacTxnAcctEntry existingEntry = getEntryOrThrow(processInstanceCode);

        upsertTransaction(existingAcct, existingEntry, req, processInstanceCode);

        updateBalanceAvailable(req, existingEntry, existingAcct);
    }

    @Override
    public void cancelEditBusiness(String processInstanceCode) {
        FacTxnAcct acct = getAcctOrThrow(processInstanceCode);
        FacTxnAcctEntry entry = getEntryOrThrow(processInstanceCode);
        List<FacTxnAcctEntryDtl> details = facTxnAcctEntryDtlRepo.getFacTxnAcctEntryDtlsByTxnAcctEntryCode(entry.getTxnAcctEntryCode());
        acct.setBusinessStatus(BUSINESS_STATUS_CANCEL);
        entry.setBusinessStatus(BUSINESS_STATUS_CANCEL);

        if (!CollectionUtils.isEmpty(details)) {
            Set<String> accNos = details.stream()
                    .map(FacTxnAcctEntryDtl::getAccNo)
                    .collect(Collectors.toSet());

            Map<String, FacInfAcc> accMap = accRepo.findByAccNoIn(accNos).stream()
                    .collect(Collectors.toMap(FacInfAcc::getAccNo, Function.identity()));

            applyRevertBalanceLogic(details, accMap);
            details.forEach(dtl -> dtl.setBusinessStatus(BUSINESS_STATUS_CANCEL));
            accRepo.saveAll(accMap.values());
            facTxnAcctEntryDtlRepo.saveAll(details);
        }
        facTxnAcctRepo.save(acct);
        facTxnAcctEntryRepo.save(entry);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTransaction(String processInstanceCode) {
        LocalDateTime now = LocalDateTime.now();
        FacTxnAcct acct = getAcctOrThrow(processInstanceCode);
        FacTxnAcctEntry entry = getEntryOrThrow(processInstanceCode);
        List<FacTxnAcctEntryDtl> details = facTxnAcctEntryDtlRepo.getFacTxnAcctEntryDtlsByTxnAcctEntryCode(entry.getTxnAcctEntryCode());

        if (CollectionUtils.isEmpty(details)) return;

        updateTransactionStatus(acct, entry, details, now);

        updateAccountBalances(details, now);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSheetInf(SheetInfoDto req) {
        FacTxnAcct existingAcct = getAcctOrThrow(req.getProcessInstanceCode());
        FacTxnAcctEntry existingEntry = getEntryOrThrow(req.getProcessInstanceCode());
        upsertTransaction(existingAcct, existingEntry, req, req.getProcessInstanceCode());
        updateBalanceAvailable(req, existingEntry, existingAcct);
    }

    private void updateTransactionStatus(FacTxnAcct acct, FacTxnAcctEntry entry, List<FacTxnAcctEntryDtl> details, LocalDateTime now) {
        acct.setBusinessStatus(STATUS_COMPLETE);
        acct.setModifiedDate(Timestamp.valueOf(now));
        facTxnAcctRepo.save(acct);
        entry.setBusinessStatus(STATUS_COMPLETE);
        entry.setModifiedDate(Timestamp.valueOf(now));
        facTxnAcctEntryRepo.save(entry);
        details.forEach(dtl -> {
            dtl.setBusinessStatus(STATUS_COMPLETE);
            dtl.setModifiedDate(Timestamp.valueOf(now));
        });
        facTxnAcctEntryDtlRepo.saveAll(details);
    }

    private void updateAccountBalances(List<FacTxnAcctEntryDtl> details, LocalDateTime now) {
        Set<String> accNos = details.stream().map(FacTxnAcctEntryDtl::getAccNo).collect(Collectors.toSet());

        Map<String, FacInfAcc> accMap = accRepo.findByAccNoIn(accNos).stream()
                .collect(Collectors.toMap(FacInfAcc::getAccNo, Function.identity()));

        Map<String, FacInfAccBal> accBalMap = accBalRepo.findByAccNoIn(accNos).stream()
                .collect(Collectors.toMap(FacInfAccBal::getAccNo, Function.identity()));

        List<FacInfAccA> accAArchiveList = new ArrayList<>();
        List<FacInfAccBalA> accBalArchiveList = new ArrayList<>();

        for (FacTxnAcctEntryDtl dtl : details) {
            FacInfAcc acc = accMap.get(dtl.getAccNo());
            FacInfAccBal accBal = accBalMap.get(dtl.getAccNo());

            if (acc == null) continue;

            BigDecimal amount = dtl.getLineForeignAmt() != null ? dtl.getLineForeignAmt() : BigDecimal.ZERO;

            calculateNewBalances(acc, accBal, dtl.getEntryType(), amount);

            acc.setModifiedDate(Timestamp.valueOf(now));

            if (accBal != null) {
                accBal.setBal(acc.getBal());
                accBal.setBalAvailable(acc.getBalAvailable());
                accBal.setBalActual(acc.getBalActual());
                accBal.setModifiedDate(Timestamp.valueOf(now));
            }

            FacInfAccA accA = new FacInfAccA();
            BeanUtils.copyProperties(acc, accA);
            accA.setDataTime(Timestamp.valueOf(now));
            accAArchiveList.add(accA);

            if (accBal != null) {
                FacInfAccBalA accBalA = new FacInfAccBalA();
                BeanUtils.copyProperties(accBal, accBalA);
                accBalA.setId(null);
                accBalA.setDataTime(Timestamp.from(Instant.now()));
                accBalArchiveList.add(accBalA);
            }
        }

        accRepo.saveAll(accMap.values());
        accBalRepo.saveAll(accBalMap.values());

        if (!accAArchiveList.isEmpty()) accARepo.saveAll(accAArchiveList);
        if (!accBalArchiveList.isEmpty()) accBalARepo.saveAll(accBalArchiveList);
    }

    private FacTxnAcct upsertTransaction(FacTxnAcct acct, FacTxnAcctEntry entry, SheetInfoDto request, String instanceCode) {
        if (acct.getId() == null) {
            acct.setProcessInstanceCode(instanceCode);
            acct.setTxnDate(LocalDate.now());
            acct.setTxnTime(LocalDateTime.now());
            acct.setCurrencyCode("VND");
            acct.setProcessTypeCode(PROCESS_TYPE_CODE);
        }
        acct.setTotalForeignAmt(request.getTotalForeignAmt());
        acct.setTxnContent(request.getTxnContent());
        acct.setOrgCode(request.getOrgCode());
        acct.setObjectTypeCode(request.getObjectTypeCode());
        acct.setObjectTxnCode(request.getObjectTxnCode());
        acct.setObjectTxnName(request.getObjectTxnName());
        acct.setIdentificationId(request.getIdentificationId());
        acct.setIssueDate(request.getIssueDate());
        acct.setIssuePlace(request.getIssuePlace());
        acct.setAddress(request.getAddress());
        acct.setBusinessStatus(DEFAULT_STATUS);
        acct.setTotalBaseAmt(request.getTotalForeignAmt());
        acct.setTotalTaxAmt(null);
        acct.setTotalFeeAmt(null);
        acct.setTotalAdjustAmt(null);
        acct.setTotalReversedAmt(null);
        FacTxnAcct savedAcct = facTxnAcctRepo.save(acct);

        boolean isNewEntry = entry.getId() == null;
        boolean voucherTypeChanged = !isNewEntry && !request.getVoucherTypeCode().equals(entry.getVoucherTypeCode());
        if (isNewEntry) {
            entry.setTxnAcctEntryCode(acct.getProcessInstanceCode() + ".001");
            entry.setReferenceCode(acct.getProcessInstanceCode());
            entry.setProcessInstanceCode(acct.getProcessInstanceCode());
            entry.setEntryTypeCode(ENTRY_TYPE_CODE_CTNB);
            entry.setCurrencyCode("VND");
            entry.setTxnDate(acct.getTxnDate());
        }
        if (isNewEntry || voucherTypeChanged) {
            String voucherNo = generateCodeService.generateVoucherCode(acct.getOrgCode(), request.getVoucherTypeCode());
            entry.setVoucherNo(voucherNo);
        }
        entry.setVoucherTypeCode(request.getVoucherTypeCode());
        entry.setOrgCode(acct.getOrgCode());
        entry.setEntryForeignAmt(acct.getTotalForeignAmt());
        entry.setEntryBaseAmt(acct.getTotalForeignAmt());
        entry.setBusinessStatus(BUSINESS_STATUS_ACTIVE);
        entry.setEntryTaxAmt(BigDecimal.ZERO);
        entry.setEntryFeeAmt(BigDecimal.ZERO);
        entry.setEntryAdjustAmt(BigDecimal.ZERO);
        entry.setEntryReversedAmt(BigDecimal.ZERO);
        facTxnAcctEntryRepo.save(entry);
        return savedAcct;
    }

    private void processBalancesForInit(SheetInfoDto request, FacTxnAcctEntry entry, FacTxnAcct acct) {
        if (CollectionUtils.isEmpty(request.getAccounts())) return;

        List<FacTxnAcctEntryDtl> dtlListToSave = new ArrayList<>();
        Set<String> accNosToUpdate = new HashSet<>();
        Set<String> accClassCodes = new HashSet<>();
        String entryType = determineEntryType(entry.getVoucherTypeCode());

        for (AccountEntryDto dto : request.getAccounts()) {
            accNosToUpdate.add(dto.getAccNo());
            accClassCodes.add(dto.getAccClassCode());
        }

        Map<String, String> coaMap = classCoaMapRepo.findByOrgCodeAndAccClassCodeIn(acct.getOrgCode(), accClassCodes)
                .stream().collect(Collectors.toMap(FacCfgAccClassCoaMap::getAccClassCode, FacCfgAccClassCoaMap::getAccCoaCode, (k1, k2) -> k1));
        Map<String, FacInfAcc> accMap = accRepo.findByAccNoIn(accNosToUpdate)
                .stream().collect(Collectors.toMap(FacInfAcc::getAccNo, Function.identity()));

        int index = 1;
        for (AccountEntryDto dto : request.getAccounts()) {
            dtlListToSave.add(buildFacTxnAcctEntryDtl(acct, entry, dto, entryType, coaMap.get(dto.getAccClassCode()), index++));

            FacInfAcc infAcc = accMap.get(dto.getAccNo());
            if (infAcc == null) throw new BusinessException(FacErrorCode.DATA_NOT_FOUND, dto.getAccNo());

            String accNature = infAcc.getAccNature();
            int factor = determineBalanceFactor(accNature, entryType);

            BigDecimal currentBal = infAcc.getBalAvailable() != null ? infAcc.getBalAvailable() : BigDecimal.ZERO;
            BigDecimal amount = dto.getLineForeignAmt() != null ? dto.getLineForeignAmt() : BigDecimal.ZERO;

            if (factor == -1 && currentBal.compareTo(amount) < 0) {
                throw new BusinessException(FacErrorCode.ERROR_BAL, "Tài khoản " + infAcc.getAccNo() + " không đủ số dư khả dụng");
            }
            BigDecimal change = amount.multiply(BigDecimal.valueOf(factor));
            infAcc.setBalAvailable(currentBal.add(change));
        }
        facTxnAcctEntryDtlRepo.saveAll(dtlListToSave);
        accRepo.saveAll(accMap.values());
    }

    private FacTxnAcctEntryDtl buildFacTxnAcctEntryDtl(FacTxnAcct acct, FacTxnAcctEntry entry, AccountEntryDto dto, String entryType,
                                                       String coaCode, int index) {
        FacTxnAcctEntryDtl dtl = new FacTxnAcctEntryDtl();
        dtl.setOrgCode(acct.getOrgCode());
        dtl.setTxnDate(acct.getTxnDate());
        dtl.setProcessInstanceCode(acct.getProcessInstanceCode());
        dtl.setBusinessStatus(BUSINESS_STATUS_ACTIVE);
        String suffix = String.format("%04d", index);
        dtl.setTxnAcctEntryDtlCode(entry.getTxnAcctEntryCode() + "." + suffix);
        dtl.setTxnAcctEntryCode(entry.getTxnAcctEntryCode());
        dtl.setAccClassCode(dto.getAccClassCode());
        dtl.setAccNo(dto.getAccNo());
        dtl.setEntryType(entryType);
        dtl.setAccCoaCode(coaCode);
        BigDecimal amount = dto.getLineForeignAmt() != null ? dto.getLineForeignAmt() : BigDecimal.ZERO;
        dtl.setLineForeignAmt(amount);
        dtl.setLineBaseAmt(amount);
        dtl.setCurrencyCode("VND");
        return dtl;
    }

    private void updateBalanceAvailable(SheetInfoDto request, FacTxnAcctEntry entry, FacTxnAcct acct) {
        revertAvailableBalanceEdit(entry.getTxnAcctEntryCode());

        Set<String> accNos = request.getAccounts().stream().map(AccountEntryDto::getAccNo).collect(Collectors.toSet());
        Set<String> accClasses = request.getAccounts().stream().map(AccountEntryDto::getAccClassCode).collect(Collectors.toSet());

        Map<String, FacInfAcc> accMap = accRepo.findByAccNoIn(accNos).stream()
                .collect(Collectors.toMap(FacInfAcc::getAccNo, Function.identity()));

        Map<String, String> coaMap = classCoaMapRepo.findByOrgCodeAndAccClassCodeIn(acct.getOrgCode(), accClasses)
                .stream()
                .collect(Collectors.toMap(FacCfgAccClassCoaMap::getAccClassCode, FacCfgAccClassCoaMap::getAccCoaCode, (k1, k2) -> k1));

        String entryType = determineEntryType(request.getVoucherTypeCode());
        List<FacTxnAcctEntryDtl> newDtls = new ArrayList<>();
        int index = 1;
        for (AccountEntryDto dto : request.getAccounts()) {
            FacInfAcc infAcc = accMap.get(dto.getAccNo());
            if (infAcc == null)
                throw new BusinessException(FacErrorCode.DATA_NOT_FOUND, "Không tìm thấy tài khoản: " + dto.getAccNo());

            BigDecimal currentActualBal = infAcc.getBalActual() != null ? infAcc.getBalActual() : BigDecimal.ZERO;
            BigDecimal amount = dto.getLineForeignAmt() != null ? dto.getLineForeignAmt() : BigDecimal.ZERO;

            // Xác định số số dư thực tế
            int factor = determineBalanceFactor(infAcc.getAccNature(), entryType);

            if (factor == -1 && currentActualBal.compareTo(amount) < 0) {
                throw new BusinessException(FacErrorCode.ERROR_BAL, "Tài khoản " + infAcc.getAccNo() + " không đủ số dư thực tế để thực hiện giao dịch.");
            }
            BigDecimal change = amount.multiply(BigDecimal.valueOf(factor));
            BigDecimal newActualBal = currentActualBal.add(change);
            infAcc.setBalActual(newActualBal);
            newDtls.add(buildFacTxnAcctEntryDtl(acct, entry, dto, entryType, coaMap.get(dto.getAccClassCode()), index++));
        }
        facTxnAcctEntryDtlRepo.saveAll(newDtls);
        accRepo.saveAll(accMap.values());
    }

    private void revertAvailableBalanceEdit(String entryCode) {
        List<FacTxnAcctEntryDtl> oldDtls = facTxnAcctEntryDtlRepo.getFacTxnAcctEntryDtlsByTxnAcctEntryCode(entryCode);
        if (CollectionUtils.isEmpty(oldDtls)) return;

        Set<String> accNos = oldDtls.stream().map(FacTxnAcctEntryDtl::getAccNo).collect(Collectors.toSet());
        Map<String, FacInfAcc> accMap = accRepo.findByAccNoIn(accNos).stream()
                .collect(Collectors.toMap(FacInfAcc::getAccNo, Function.identity()));

        applyRevertBalanceLogic(oldDtls, accMap);
        accRepo.saveAll(accMap.values());
        facTxnAcctEntryDtlRepo.deleteAll(oldDtls);
    }

    private void applyRevertBalanceLogic(Collection<FacTxnAcctEntryDtl> details, Map<String, FacInfAcc> accMap) {
        for (FacTxnAcctEntryDtl dtl : details) {
            FacInfAcc acc = accMap.get(dtl.getAccNo());
            if (acc != null) {
                BigDecimal current = acc.getBalAvailable() != null ? acc.getBalAvailable() : BigDecimal.ZERO;
                BigDecimal amount = dtl.getLineForeignAmt() != null ? dtl.getLineForeignAmt() : BigDecimal.ZERO;

                if (ENTRY_TYPE_DEBIT.equals(dtl.getEntryType())) {
                    acc.setBalAvailable(current.subtract(amount));
                } else if (ENTRY_TYPE_CREDIT.equals(dtl.getEntryType())) {
                    acc.setBalAvailable(current.add(amount));
                }
            }
        }
    }

    private FacTxnAcct getAcctOrThrow(String processInstanceCode) {
        return facTxnAcctRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, processInstanceCode));
    }


    private FacTxnAcctEntry getEntryOrThrow(String processInstanceCode) {
        return facTxnAcctEntryRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, processInstanceCode));
    }

    private String determineEntryType(String voucherCode) {
        if (VOUCHER_TYPE_IMPORT.equals(voucherCode)) return ENTRY_TYPE_DEBIT;
        if (VOUCHER_TYPE_EXPORT.equals(voucherCode)) return ENTRY_TYPE_CREDIT;
        return "N/A";
    }

    private void calculateNewBalances(FacInfAcc acc, FacInfAccBal accBal, String entryType, BigDecimal amount) {
        BigDecimal currentBal = acc.getBal() != null ? acc.getBal() : BigDecimal.ZERO;
        BigDecimal currentActual = acc.getBalActual() != null ? acc.getBalActual() : BigDecimal.ZERO;

        if (ENTRY_TYPE_CREDIT.equals(entryType)) {
            acc.setBal(currentBal.subtract(amount));
            acc.setBalActual(currentActual.subtract(amount));

            if (accBal != null) {
                BigDecimal crAmt = accBal.getTotalCrAmt() != null ? accBal.getTotalCrAmt() : BigDecimal.ZERO;
                accBal.setTotalCrAmt(crAmt.add(amount));
            }
        } else if (ENTRY_TYPE_DEBIT.equals(entryType)) {
            acc.setBal(currentBal.add(amount));
            acc.setBalActual(currentActual.add(amount));

            if (accBal != null) {
                BigDecimal drAmt = accBal.getTotalDrAmt() != null ? accBal.getTotalDrAmt() : BigDecimal.ZERO;
                accBal.setTotalDrAmt(drAmt.add(amount));
            }
        }
    }

    private int determineBalanceFactor(String nature, String entryType) {
        // Trường hợp GIẢM số dư (-1)
        // 1. Nature D (Dư Nợ)  & Entry C (Ghi Có)
        // 2. Nature C (Dư Có)  & Entry D (Ghi Nợ)
        // 3. Nature B (Lưỡng tính) & Entry C (Ghi Có)
        if (("D".equals(nature) && "C".equals(entryType)) ||
                ("C".equals(nature) && "D".equals(entryType)) ||
                ("B".equals(nature) && "C".equals(entryType))) {
            return -1;
        }

        // Trường hợp TĂNG số dư (+1)
        // 1. Nature D (Dư Nợ)  & Entry D (Ghi Nợ)
        // 2. Nature C (Dư Có)  & Entry C (Ghi Có)
        // 3. Nature B (Lưỡng tính) & Entry D (Ghi Nợ)
        if (("D".equals(nature) && "D".equals(entryType)) ||
                ("C".equals(nature) && "C".equals(entryType)) ||
                ("B".equals(nature) && "D".equals(entryType))) {
            return 1;
        }
        return 0;
    }
}
