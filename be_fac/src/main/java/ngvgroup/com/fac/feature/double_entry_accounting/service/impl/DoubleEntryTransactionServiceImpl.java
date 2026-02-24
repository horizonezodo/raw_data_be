package ngvgroup.com.fac.feature.double_entry_accounting.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.AllArgsConstructor;
import ngvgroup.com.fac.core.constant.FacErrorCode;
import ngvgroup.com.fac.core.constant.FacVariableConstants;
import ngvgroup.com.fac.feature.common.component.GenerateCodeService;
import ngvgroup.com.fac.feature.double_entry_accounting.dto.DoubleEntryAccountingProcessDto;
import ngvgroup.com.fac.feature.double_entry_accounting.service.DoubleEntryTransactionService;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAcc;
import ngvgroup.com.fac.feature.fac_inf_acc.model.FacInfAccA;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccARepository;
import ngvgroup.com.fac.feature.fac_inf_acc.repository.FacInfAccRepository;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct.FacTxnAcctDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry.FacTxnAcctEntryDTO;
import ngvgroup.com.fac.feature.single_entry_acct.dto.fac_txn_acct_entry_dtl.FacTxnAcctEntryDtlDTO;
import ngvgroup.com.fac.feature.single_entry_acct.mapper.FacTxnAcctEntryDtlMapper;
import ngvgroup.com.fac.feature.single_entry_acct.mapper.FacTxnAcctEntryMapper;
import ngvgroup.com.fac.feature.single_entry_acct.mapper.FacTxnAcctMapper;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcct;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntry;
import ngvgroup.com.fac.feature.single_entry_acct.model.FacTxnAcctEntryDtl;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryDtlRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctEntryRepository;
import ngvgroup.com.fac.feature.single_entry_acct.repository.FacTxnAcctRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoubleEntryTransactionServiceImpl implements DoubleEntryTransactionService {

    private final FacTxnAcctEntryRepository facTxnAcctEntryRepository;
    private final FacTxnAcctRepository facTxnAcctRepository;
    private final FacTxnAcctEntryDtlRepository facTxnAcctEntryDtlRepository;
    private final FacTxnAcctMapper facTxnAcctMapper;
    private final FacTxnAcctEntryMapper facTxnAcctEntryMapper;
    private final FacTxnAcctEntryDtlMapper facTxnAcctEntryDtlMapper;
    private final GenerateCodeService generateCodeService;
    private final FacInfAccRepository facInfAccRepository;
    private final FacInfAccARepository facInfAccARepository;


    @Override
    public void createDoubleAccountEntry(DoubleEntryAccountingProcessDto request) {
        if(request==null) return;
        String txnAcctEntryCode=removeLastDate(generateCodeService.generateTxnAcctEntryCode(request.getFacTxnAcctDTO().getOrgCode(),request.getFacTxnAcctDTO().getProcessInstanceCode()));

        createAccounting(request.getFacTxnAcctDTO(),request.getFacTxnAcctDTO().getProcessInstanceCode());
        createAcctEntry(request.getFacTxnAcctEntryDto(),request.getFacTxnAcctDTO().getProcessInstanceCode(),txnAcctEntryCode);

        createAcctEntryDtl(request.getFacTxnAcctEntryDtlDtos(),request.getFacTxnAcctDTO().getProcessInstanceCode(),txnAcctEntryCode,request.getFacTxnAcctDTO().getOrgCode());
        updateInfAcc(request.getFacTxnAcctDTO().getProcessInstanceCode(),FacVariableConstants.COMPLETE);
    }


    private void createAccounting(FacTxnAcctDTO facTxnAcctDTO, String processInstanceCode){

        if(facTxnAcctDTO==null) return;
        facTxnAcctDTO.setProcessInstanceCode(processInstanceCode);
        facTxnAcctDTO.setProcessTypeCode(FacVariableConstants.DOUBLE_ACCT_PROCESS_TYPE_CODE);
        facTxnAcctRepository.save(facTxnAcctMapper.toEntity(facTxnAcctDTO));

    }

    private void createAcctEntry(FacTxnAcctEntryDTO facTxnAcctEntryDto, String processInstanceCode,String txnAcctEntryCode){

        if(facTxnAcctEntryDto==null) return;

        String voucherNo = generateCodeService.generateVoucherCode(
                facTxnAcctEntryDto.getOrgCode(),
                facTxnAcctEntryDto.getVoucherTypeCode());
        facTxnAcctEntryDto.setProcessInstanceCode(processInstanceCode);
        facTxnAcctEntryDto.setReferenceCode(processInstanceCode);
        facTxnAcctEntryDto.setVoucherNo(voucherNo);
        facTxnAcctEntryDto.setTxnAcctEntryCode(txnAcctEntryCode);
        facTxnAcctEntryRepository.save(facTxnAcctEntryMapper.toEntity(facTxnAcctEntryDto));

    }

    private String removeLastDate(String code) {
        if (code == null) return null;
        return code.replaceFirst("\\.\\d{8}(?=\\.\\d+$)", "");
    }


    private void createAcctEntryDtl(
            List<FacTxnAcctEntryDtlDTO> facTxnAcctEntryDtlDtos,
            String processInstanceCode,
            String txnAcctEntryCode,
            String orgCode
    ) {

        if (facTxnAcctEntryDtlDtos == null || facTxnAcctEntryDtlDtos.isEmpty()) {
            return;
        }

        List<FacTxnAcctEntryDtl> entities = facTxnAcctEntryDtlDtos.stream()
                .map(dto -> {
                    dto.setProcessInstanceCode(processInstanceCode);
                    dto.setTxnAcctEntryDtlCode(
                            removeLastDate(
                                    generateCodeService.generateTxnAcctEntryDtlCode(
                                            orgCode, txnAcctEntryCode
                                    )
                            )
                    );
                    dto.setTxnAcctEntryCode(txnAcctEntryCode);

                    return facTxnAcctEntryDtlMapper.toEntity(dto);
                })
                .toList();

        facTxnAcctEntryDtlRepository.saveAll(entities);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDoubleEntryAcctEndProcess(String processInstanceCode){
        updateInfAcc(processInstanceCode,FacVariableConstants.APPROVE);
        insertFacInfAccA(processInstanceCode);
    }

    private void updateInfAcc(String processInstanceCode,String status) {
        List<FacTxnAcctEntryDtl> details = facTxnAcctEntryDtlRepository.getAllByProcessInstanceCode(processInstanceCode);
        if (details.isEmpty()) {
            return;
        }

        for (FacTxnAcctEntryDtl f : details) {
            FacInfAcc facInfAcc = facInfAccRepository.getFacInfAccByAccNo(f.getAccNo());
            BigDecimal amt = f.getLineForeignAmt();


            if (facInfAcc == null || amt == null) {
                continue;
            }

            BigDecimal updatedBalance = processEntry(facInfAcc, f, amt);

            if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException(ErrorCode.OK,
                        "Số tiền phát sinh đang vượt quá Số dư thực tế (TK: " + f.getAccNo() + ")");
            }

            if(isAddScenario(facInfAcc.getAccNature(),f.getEntryType())){
                facInfAcc.setBalAvailable(
                        facInfAcc.getBalAvailable().add(f.getLineForeignAmt())
                );
            }

            if(isSubtractScenario(facInfAcc.getAccNature(),f.getEntryType()) ){
                facInfAcc.setBalAvailable(facInfAcc.getBalAvailable().subtract(f.getLineForeignAmt()));
            }


            if(isAddScenario(facInfAcc.getAccNature(),f.getEntryType())&& FacVariableConstants.REJECT.equals(status)){
                facInfAcc.setBalAvailable(
                        facInfAcc.getBalAvailable().subtract(f.getLineForeignAmt())
                );
            }

            if(isSubtractScenario(facInfAcc.getAccNature(),f.getEntryType())&& FacVariableConstants.REJECT.equals(status)){
                facInfAcc.setBalAvailable(facInfAcc.getBalAvailable().add(f.getLineForeignAmt()));
            }

            if(FacVariableConstants.APPROVE.equals(status)){
                facInfAcc.setBalActual(updatedBalance);
            }
            facInfAccRepository.save(facInfAcc);
        }
    }

    private BigDecimal processEntry(FacInfAcc facInfAcc, FacTxnAcctEntryDtl f, BigDecimal amt) {
        String accNature = facInfAcc.getAccNature();
        String entryType = f.getEntryType();

        if (isSubtractScenario(accNature, entryType)) {
            BigDecimal balActual = nullToZero(facInfAcc.getBalActual());
            balActual = balActual.subtract(amt);

            return balActual;

        } else if (isAddScenario(accNature, entryType)) {
            BigDecimal balAvailable = nullToZero(facInfAcc.getBalAvailable());
            balAvailable = balAvailable.add(amt);

            return balAvailable;

        } else {
            throw new BusinessException(ErrorCode.OK,
                    "ACC_NATURE hoặc ENTRY_TYPE không hợp lệ (accNo=" + f.getAccNo() + ")");
        }
    }

    private boolean isSubtractScenario(String accNature, String entryType) {
        return (FacVariableConstants.DEBIT.equals(accNature) && FacVariableConstants.CREDIT.equals(entryType))
                || (FacVariableConstants.CREDIT.equals(accNature) && FacVariableConstants.DEBIT.equals(entryType))
                || (FacVariableConstants.BOTH.equals(accNature) && FacVariableConstants.CREDIT.equals(entryType));
    }

    private boolean isAddScenario(String accNature, String entryType) {
        return (FacVariableConstants.DEBIT.equals(accNature) && FacVariableConstants.DEBIT.equals(entryType))
                || (FacVariableConstants.CREDIT.equals(accNature) && FacVariableConstants.CREDIT.equals(entryType))
                || (FacVariableConstants.BOTH.equals(accNature) && FacVariableConstants.DEBIT.equals(entryType));
    }


    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }


    private void insertFacInfAccA(String processInstanceCode) {

        List<FacTxnAcctEntryDtl> details =
                facTxnAcctEntryDtlRepository.getAllByProcessInstanceCode(processInstanceCode);

        if (details == null || details.isEmpty()) {
            return;
        }

        Set<String> accNos = details.stream()
                .map(FacTxnAcctEntryDtl::getAccNo)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        if (accNos.isEmpty()) {
            return;
        }

        List<FacInfAcc> facInfAccs = facInfAccRepository.findByAccNoIn(accNos);

        if (facInfAccs.isEmpty()) {
            return;
        }

        List<FacInfAccA> facInfAccAs = facInfAccs.stream()
                .map(acc -> {
                    FacInfAccA accA = new FacInfAccA();
                    BeanUtils.copyProperties(
                            acc,
                            accA,
                            getNullPropertyNames(acc)
                    );
                    accA.setId(null);
                    accA.setDataTime(Timestamp.from(Instant.now()));

                    return accA;
                })
                .toList();

        facInfAccARepository.saveAll(facInfAccAs);

    }

    public String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }

    @Override
    public void cancelTask(String processInstanceCode){
        Optional<FacTxnAcct> facTxnAcct=facTxnAcctRepository.findByProcessInstanceCode(processInstanceCode);
        if (facTxnAcct.isPresent()){
            facTxnAcct.get().setBusinessStatus(FacVariableConstants.CANCEL);
        }
        Optional<FacTxnAcctEntry> facTxnAcctEntry=facTxnAcctEntryRepository.findByProcessInstanceCode(processInstanceCode);
        if(facTxnAcctEntry.isPresent()){
            facTxnAcctEntry.get().setBusinessStatus(FacVariableConstants.CANCEL);
        }

        List<FacTxnAcctEntryDtl> facTxnAcctEntryDtl=facTxnAcctEntryDtlRepository.getAllByProcessInstanceCode(processInstanceCode);
        if(facTxnAcctEntryDtl!=null && facTxnAcctEntryDtl.isEmpty()){
            for (FacTxnAcctEntryDtl f:facTxnAcctEntryDtl){
                f.setBusinessStatus(FacVariableConstants.CANCEL);
            }
        }
    }

    @Override
    public DoubleEntryAccountingProcessDto getDetail(String processInstanceCode){

        DoubleEntryAccountingProcessDto dto=new DoubleEntryAccountingProcessDto();
        FacTxnAcctDTO facTxnAcctDTO=new FacTxnAcctDTO();
        FacTxnAcctEntryDTO facTxnAcctEntryDto=new FacTxnAcctEntryDTO();


        Optional<FacTxnAcct> facTxnAcct=facTxnAcctRepository.findByProcessInstanceCode(processInstanceCode);
        if(facTxnAcct.isPresent()){
            facTxnAcctDTO=facTxnAcctMapper.toDto(facTxnAcct.get());

        }

        Optional<FacTxnAcctEntry> facTxnAcctEntry=facTxnAcctEntryRepository.findByProcessInstanceCode(processInstanceCode);
        if(facTxnAcctEntry.isPresent()){
            facTxnAcctEntryDto=facTxnAcctEntryMapper.toDto(facTxnAcctEntry.get());

        }

        List<FacTxnAcctEntryDtlDTO> facTxnAcctEntryDtlDTOS=facTxnAcctEntryDtlRepository.getDetailDto(processInstanceCode);

        dto.setFacTxnAcctDTO(facTxnAcctDTO);
        dto.setFacTxnAcctEntryDto(facTxnAcctEntryDto);
        dto.setFacTxnAcctEntryDtlDtos(facTxnAcctEntryDtlDTOS);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDoubleEntry(DoubleEntryAccountingProcessDto request,String processInstanceCode){
        FacTxnAcct facTxnAcct=facTxnAcctRepository.findByProcessInstanceCode(processInstanceCode).orElseThrow(()->new BusinessException(FacErrorCode.PROCESS_NOT_FOUND,
                "Không tìm thấy hồ sơ: " + processInstanceCode));

            BeanUtils.copyProperties(
                    request.getFacTxnAcctDTO(),
                    facTxnAcct,
                    getNullPropertyNames(request.getFacTxnAcctDTO())
            );

       FacTxnAcctEntry facTxnAcctEntry=facTxnAcctEntryRepository.findByProcessInstanceCode(processInstanceCode).orElseThrow(()->new BusinessException(FacErrorCode.PROCESS_NOT_FOUND,
               "Không tìm thấy hồ sơ: " + processInstanceCode));

            BeanUtils.copyProperties(
                    request.getFacTxnAcctEntryDto(),
                    facTxnAcctEntry,
                    getNullPropertyNames(request.getFacTxnAcctEntryDto())
            );

        List<FacTxnAcctEntryDtl> entities =
                facTxnAcctEntryDtlRepository.getAllByProcessInstanceCode(processInstanceCode);

        if (entities.isEmpty()) {
            throw new BusinessException(
                    FacErrorCode.PROCESS_NOT_FOUND,
                    "Không tìm thấy hồ sơ: " + processInstanceCode
            );
        }

        FacTxnAcctEntryDtl sampleEntity = entities.stream()
                .filter(e -> e.getId() != null)
                .findFirst()
                .orElseThrow(() -> new BusinessException(
                        FacErrorCode.PROCESS_NOT_FOUND,
                        "Không có dữ liệu hợp lệ cho hồ sơ: " + processInstanceCode
                ));

        Set<Long> requestIds = request.getFacTxnAcctEntryDtlDtos().stream()
                .map(FacTxnAcctEntryDtlDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<FacTxnAcctEntryDtl> entitiesToRemove = entities.stream()
                .filter(e -> e.getId() != null)
                .filter(e -> !requestIds.contains(e.getId()))
                .toList();

        if (!entitiesToRemove.isEmpty()) {
            facTxnAcctEntryDtlRepository.deleteAll(entitiesToRemove);
            entities.removeAll(entitiesToRemove);
        }

        Map<Long, FacTxnAcctEntryDtl> entityMap = entities.stream()
                .collect(Collectors.toMap(
                        FacTxnAcctEntryDtl::getId,
                        Function.identity()
                ));


        for (FacTxnAcctEntryDtlDTO dto : request.getFacTxnAcctEntryDtlDtos()) {

            FacTxnAcctEntryDtl entity = entityMap.get(dto.getId());

            if (entity != null) {

                BeanUtils.copyProperties(dto, entity, getNullPropertyNames(dto));
            } else {

                FacTxnAcctEntryDtl newEntity = new FacTxnAcctEntryDtl();

                newEntity.setTxnAcctEntryDtlCode(sampleEntity.getTxnAcctEntryDtlCode());
                newEntity.setTxnAcctEntryCode(sampleEntity.getTxnAcctEntryCode());
                newEntity.setOrgCode(sampleEntity.getOrgCode());
                newEntity.setBusinessStatus(sampleEntity.getBusinessStatus());
                newEntity.setTxnDate(sampleEntity.getTxnDate());
                newEntity.setProcessInstanceCode(processInstanceCode);

                BeanUtils.copyProperties(dto, newEntity, getNullPropertyNames(dto));
                entities.add(newEntity);
            }
        }
        facTxnAcctEntryDtlRepository.saveAll(entities);

        facTxnAcctRepository.save(facTxnAcct);
        facTxnAcctEntryRepository.save(facTxnAcctEntry);

        updateInfAcc(processInstanceCode,FacVariableConstants.COMPLETE);
    }

    @Override
    public void updateBusinessStatus(String processInstanceCode,String businessStatus){


        Optional<FacTxnAcct> facTxnAcct=facTxnAcctRepository.findByProcessInstanceCode(processInstanceCode);
        if(facTxnAcct.isPresent()){
            facTxnAcct.get().setBusinessStatus(businessStatus);
        }

        Optional<FacTxnAcctEntry> facTxnAcctEntry=facTxnAcctEntryRepository.findByProcessInstanceCode(processInstanceCode);
        if(facTxnAcctEntry.isPresent()){
            facTxnAcctEntry.get().setBusinessStatus(businessStatus);
        }

        List<FacTxnAcctEntryDtl> facTxnAcctEntryDtl =
                facTxnAcctEntryDtlRepository.getAllByProcessInstanceCode(processInstanceCode);

        if (facTxnAcctEntryDtl != null && !facTxnAcctEntryDtl.isEmpty()) {
            facTxnAcctEntryDtl.forEach(dtl ->
                    dtl.setBusinessStatus(businessStatus)
            );
        }

        if(FacVariableConstants.REJECT.equals(businessStatus)){
            updateInfAcc(processInstanceCode,businessStatus);
        }

        facTxnAcctRepository.save(facTxnAcct.get());
        facTxnAcctEntryRepository.save(facTxnAcctEntry.get());
        facTxnAcctEntryDtlRepository.saveAll(facTxnAcctEntryDtl);

    }
}
