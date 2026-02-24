package ngvgroup.com.loan.feature.product_proccess.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.core.constant.VariableConstants;
import ngvgroup.com.loan.feature.product_proccess.mapper.LnmTxnProductMapper;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProduct;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProductDtl;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProduct;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProductDtl;
import ngvgroup.com.loan.feature.product_proccess.repository.cfg.LnmCfgProductDtlRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.cfg.LnmCfgProductRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.txn.LnmTxnProductDtlRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.txn.LnmTxnProductRepository;
import ngvgroup.com.loan.feature.product_proccess.service.ProductRegisterService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductRegisterServiceImpl implements ProductRegisterService {
    private final LnmTxnProductRepository lnmTxnProductRepository;
    private final LnmTxnProductMapper lnmTxnProductMapper;
    private final LnmCfgProductRepository lnmCfgProductRepository;
    private final LnmTxnProductDtlRepository lnmTxnProductDtlRepository;
    private final LnmCfgProductDtlRepository lnmCfgProductDtlRepository;

    @Override
    @Transactional
    public void endProcess(String processInstanceCode) {
        LnmTxnProduct txnProduct = lnmTxnProductRepository
                .findByProcessInstanceCode(processInstanceCode)
                .orElseThrow(() -> new BusinessException(LoanErrorCode.NOT_FOUND));

        LnmCfgProduct cfgProduct = lnmTxnProductMapper.toCfg(txnProduct);
        cfgProduct.setRecordStatus(LoanVariableConstants.APPROVAL);
        lnmCfgProductRepository.save(cfgProduct);

        List<LnmTxnProductDtl> txnDetails = lnmTxnProductDtlRepository
                .findByProcessInstanceCode(processInstanceCode);

        if (!txnDetails.isEmpty()) {
            List<LnmCfgProductDtl> cfgDetails = txnDetails.stream()
                    .map(lnmTxnProductMapper::toCfgDtl)
                    .toList();
            lnmCfgProductDtlRepository.saveAll(cfgDetails);
        }

        txnProduct.setBusinessStatus(VariableConstants.COMPLETE);
        lnmTxnProductRepository.save(txnProduct);
    }
}
