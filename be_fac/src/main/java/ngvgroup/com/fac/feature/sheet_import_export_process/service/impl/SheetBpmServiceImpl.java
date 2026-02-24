package ngvgroup.com.fac.feature.sheet_import_export_process.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.dto.request.StartRequest;
import ngvgroup.com.bpm.client.dto.request.SubmitTaskRequest;
import ngvgroup.com.bpm.client.dto.variable.MailVariableDto;
import ngvgroup.com.bpm.client.dto.variable.ProcessData;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.repository.ComInfOrganizationRepository;
import ngvgroup.com.fac.feature.fac_cfg_voucher.repository.FacCfgVoucherRuleN0Repository;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccRepository;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.AccountEntryDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.dto.SheetInfoDto;
import ngvgroup.com.fac.feature.sheet_import_export_process.service.SheetBpmService;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryDtlRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static ngvgroup.com.bpm.client.utils.BpmClientSecurityUtils.getCurrentUserName;

@Service
@RequiredArgsConstructor
public class SheetBpmServiceImpl implements SheetBpmService {
    private final FacTxnAcctRepository facTxnAcctRepo;
    private final FacTxnAcctEntryRepository facTxnAcctEntryRepo;
    private final FacTxnAcctEntryDtlRepository facTxnAcctEntryDtlRepo;
    private final FacInfAccRepository accRepo;
    private final ComInfOrganizationRepository orgRepo;
    private final FacCfgVoucherRuleN0Repository voucherRuleN0Repo;


    @Override
    public MailVariableDto buildMailVariable(String templateCode, SubmitTaskRequest<SheetInfoDto> dto) {
        MailVariableDto mailVariable = new MailVariableDto();

        String finalTemplateCode = (templateCode != null) ? templateCode : FacVariableConstants.MAIL_TEMPLATE_REJECT_SHEET;
        mailVariable.setEmailTemplateCode(finalTemplateCode);
        Map<String, String> params = new HashMap<>();

        params.put(FacVariableConstants.MAIL_PROCESS_INSTANCE_CODE,
                dto.getBpmData().getProcessInstanceCode());

        params.put(FacVariableConstants.MAIL_PARAM_APPROVER,
                getCurrentUserName());

        params.put(FacVariableConstants.MAIL_PARAM_COMMENT,
                dto.getBpmData().getTaskComment());

        mailVariable.setParamEmail(params);

        mailVariable.setUserNameCc(Collections.singletonList(getCurrentUserName()));

        return mailVariable;
    }

    @Override
    @Transactional(readOnly = true)
    public SheetInfoDto getApprovalDetail(String processInstanceCode) {
        FacTxnAcct txn = getAcctOrThrow(processInstanceCode);
        FacTxnAcctEntry entry = getEntryOrThrow(processInstanceCode);
        String orgName = orgRepo.findOrgNameByOrgCode(txn.getOrgCode());
        return SheetInfoDto.builder()
                .voucherTypeCode(entry.getVoucherTypeCode())
                .totalForeignAmt(txn.getTotalForeignAmt())
                .txnDate(txn.getTxnDate())
                .txnContent(txn.getTxnContent())
                .orgCode(orgName)
                .txnTime(txn.getTxnTime() != null
                        ? txn.getTxnTime()
                        : null)
                .objectTypeCode(txn.getObjectTypeCode())
                .objectTxnCode(txn.getObjectTxnCode())
                .objectTxnName(txn.getObjectTxnName())
                .identificationId(txn.getIdentificationId())
                .issueDate(txn.getIssueDate() != null
                        ? txn.getIssueDate()
                        : null)
                .issuePlace(txn.getIssuePlace())
                .address(txn.getAddress())
                .accounts(buildAccountsApprove(entry))
                .processInstanceCode(processInstanceCode)
                .build();
    }

    @Override
    @Transactional
    public ProcessData buildProcessData(StartRequest<SheetInfoDto> dto) {
        SheetInfoDto inf = dto.getBusinessData();
        ProcessData processData = new ProcessData();
        processData.setOrgCode(inf.getOrgCode());
        processData.setTxnContent(inf.getTxnContent());
        processData.setProcessInstanceCode(inf.getProcessInstanceCode());
        return processData;
    }

    private FacTxnAcctEntry getEntryOrThrow(String processInstanceCode) {
        return facTxnAcctEntryRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, processInstanceCode));
    }

    private FacTxnAcct getAcctOrThrow(String processInstanceCode) {
        return facTxnAcctRepo.findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(FacErrorCode.DATA_NOT_FOUND, processInstanceCode));
    }

    private List<AccountEntryDto> buildAccountsApprove(FacTxnAcctEntry entry) {
        String voucherTypeName = voucherRuleN0Repo.getVoucherTypeNameByVoucherTypeCode(entry.getVoucherTypeCode());
        List<FacTxnAcctEntryDtl> dtls = facTxnAcctEntryDtlRepo.getFacTxnAcctEntryDtlsByTxnAcctEntryCode(entry.getTxnAcctEntryCode());
        List<AccountEntryDto> list = new ArrayList<>();
        for (FacTxnAcctEntryDtl item : dtls) {
            FacInfAcc acc = accRepo.findByAccNo(item.getAccNo()).orElse(new FacInfAcc());
            list.add(AccountEntryDto.builder()
                    .voucherNo(entry.getVoucherNo())
                    .accNo(item.getAccNo())
                    .balAvailable(acc.getBalAvailable())
                    .balActual(acc.getBalActual())
                    .voucherTypeName(voucherTypeName)
                    .accClassCode(item.getAccClassCode())
                    .entryType(item.getEntryType())
                    .lineForeignAmt(item.getLineForeignAmt())
                    .build()
            );
        }
        return list;
    }


}
