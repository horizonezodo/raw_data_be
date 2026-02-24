package ngvgroup.com.loan.feature.product_proccess.service.impl;

import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ngvgroup.com.bpm.client.constant.VariableConstants;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.core.utils.GenerateNextSequence;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;
import ngvgroup.com.loan.feature.product_proccess.mapper.LnmTxnProductMapper;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProduct;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProductDtl;
import ngvgroup.com.loan.feature.product_proccess.repository.txn.LnmTxnProductDtlRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.txn.LnmTxnProductRepository;
import ngvgroup.com.loan.feature.product_proccess.service.ProductRegisterService;
import ngvgroup.com.loan.feature.product_proccess.service.ProductTransactionService;
import ngvgroup.com.loan.feature.product_proccess.service.ProductEditService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductTransactionServiceImpl implements ProductTransactionService {

    private final LnmTxnProductRepository txnRepo;
    private final LnmTxnProductMapper mapper;
    private final LnmTxnProductDtlRepository lnmTxnProductDtlRepository;
    private final LnmTxnProductMapper lnmTxnProductMapper;
    private final ProductRegisterService productRegisterService;
    private final ProductEditService productEditService;

    @Override
    public void createProduct(ProductProfileDTO profile) {
        saveTxnProduct(profile, profile.getProcessInstanceCode(), null);

        saveOrUpdateProductDetails(profile, profile.getProcessInstanceCode(), false);
    }

    @Override
    public void updateProduct(ProductProfileDTO profile) {
        LnmTxnProduct txn = txnRepo.findByLnmProductCode(profile.getLnmProductCode(), List.of(VariableConstants.CANCEL, VariableConstants.COMPLETE)).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        mapper.updateTxn(profile, txn);
        txnRepo.save(txn);

        saveOrUpdateProductDetails(profile, profile.getProcessInstanceCode(), true);
    }

    @Override
    public void updateProduct(String processInstanceCode, ProductProfileDTO profile) {
        LnmTxnProduct txn = txnRepo.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        mapper.updateTxn(profile, txn);
        txnRepo.save(txn);
        saveOrUpdateProductDetails(profile, profile.getProcessInstanceCode(), true);

    }

    @Override
    public ProductProfileDTO getDetail(String processInstanceCode) {
        LnmTxnProduct txn = txnRepo.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        ProductProfileDTO dto = lnmTxnProductMapper.toDto(txn);
        List<LnmTxnProductDtl> details = lnmTxnProductDtlRepository.findByProcessInstanceCode(processInstanceCode);
        dto.setProductDetails(details.stream()
                .map(lnmTxnProductMapper::toDtlDTO)
                .toList());

        return dto;
    }

    @Override
    public void cancelRequest(String processInstanceCode) {
        LnmTxnProduct product = txnRepo.findByProcessInstanceCode(processInstanceCode).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));
        product.setBusinessStatus(VariableConstants.CANCEL);
        txnRepo.save(product);
    }

    @Override
    public void updateEndProcess(String processInstanceCode, String type) {
        if (type.equalsIgnoreCase(LoanVariableConstants.PROCESS_REGISTER_TYPE)) {
            productRegisterService.endProcess(processInstanceCode);
        } else if (type.equalsIgnoreCase(LoanVariableConstants.PROCESS_EDIT_TYPE)) {
            productEditService.endProcess(processInstanceCode);
        }
    }

    private void saveTxnProduct(
            ProductProfileDTO profile,
            String processInstanceCode,
            Long id
    ) {
        LnmTxnProduct entity = mapper.toEntity(profile);
        entity.setProcessInstanceCode(processInstanceCode);

        if (id != null) {
            entity.setId(id);
        }

        txnRepo.save(entity);
    }

    private void saveOrUpdateProductDetails(ProductProfileDTO profile, String processInstanceCode, boolean isUpdateMode) {

        if (CollectionUtils.isEmpty(profile.getProductDetails())) {
            return;
        }

        if (isUpdateMode) {
            updateProductDetails(profile, processInstanceCode);
        } else {
            createProductDetails(profile, processInstanceCode);
        }
    }

    private void updateProductDetails(ProductProfileDTO profile,
                                      String processInstanceCode) {

        List<LnmTxnProductDtl> dbList =
                lnmTxnProductDtlRepository.findByProcessInstanceCode(processInstanceCode);

        Map<Long, LnmTxnProductDtl> dbMap = mapDbDetailsById(dbList);

        deleteRemovedDetails(profile, dbList);

        upsertDetails(profile, processInstanceCode, dbMap);
    }

    private Map<Long, LnmTxnProductDtl> mapDbDetailsById(List<LnmTxnProductDtl> dbList) {
        return dbList.stream()
                .filter(e -> e.getId() != null)
                .collect(Collectors.toMap(
                        LnmTxnProductDtl::getId,
                        Function.identity()
                ));
    }

    private void deleteRemovedDetails(ProductProfileDTO profile,
                                      List<LnmTxnProductDtl> dbList) {

        Set<Long> dtoIds = profile.getProductDetails().stream()
                .map(ngvgroup.com.loan.feature.product_proccess.dto.LnmTxnProductDtlDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        List<LnmTxnProductDtl> toDelete = dbList.stream()
                .filter(e -> e.getId() != null)
                .filter(e -> !dtoIds.contains(e.getId()))
                .toList();

        if (!toDelete.isEmpty()) {
            lnmTxnProductDtlRepository.deleteAll(toDelete);
        }
    }

    private void upsertDetails(ProductProfileDTO profile,
                               String processInstanceCode,
                               Map<Long, LnmTxnProductDtl> dbMap) {

        for (var dto : profile.getProductDetails()) {
            if (dto.getId() == null) {
                insertDetail(dto, profile, processInstanceCode);
            } else {
                updateDetail(dto, profile, processInstanceCode, dbMap);
            }
        }
    }

    private void insertDetail(
            ngvgroup.com.loan.feature.product_proccess.dto.LnmTxnProductDtlDTO dto,
            ProductProfileDTO profile,
            String processInstanceCode) {

        LnmTxnProductDtl detail = new LnmTxnProductDtl();
        setProductDetailFields(detail, dto, profile, processInstanceCode);
        lnmTxnProductDtlRepository.save(detail);
    }

    private void updateDetail(
            ngvgroup.com.loan.feature.product_proccess.dto.LnmTxnProductDtlDTO dto,
            ProductProfileDTO profile,
            String processInstanceCode,
            Map<Long, LnmTxnProductDtl> dbMap) {

        LnmTxnProductDtl detail = dbMap.get(dto.getId());
        if (detail == null) {
            return;
        }

        setProductDetailFields(detail, dto, profile, processInstanceCode);
        lnmTxnProductDtlRepository.save(detail);
    }

    private void createProductDetails(ProductProfileDTO profile,
                                      String processInstanceCode) {

        List<LnmTxnProductDtl> details = profile.getProductDetails().stream()
                .map(dto -> {
                    LnmTxnProductDtl detail = new LnmTxnProductDtl();
                    setProductDetailFields(detail, dto, profile, processInstanceCode);
                    return detail;
                })
                .toList();

        lnmTxnProductDtlRepository.saveAll(details);
    }

    /**
     * Set các trường từ DTO vào Entity
     */
    private void setProductDetailFields(LnmTxnProductDtl detail,
                                        ngvgroup.com.loan.feature.product_proccess.dto.LnmTxnProductDtlDTO dto,
                                        ProductProfileDTO profile,
                                        String processInstanceCode) {
        mapper.updateDtlEntity(dto, detail);
        detail.setOrgCode(profile.getOrgCode());
        detail.setLnmProductCode(profile.getLnmProductCode());
        detail.setProcessInstanceCode(processInstanceCode);
    }

    @Getter
    private final GenerateNextSequence sequence;
}
