package ngvgroup.com.loan.feature.product_proccess.service.impl;

import com.ngvgroup.bpm.core.common.dto.ResponseData;
import com.ngvgroup.bpm.core.common.excel.ExportExcel;
import com.ngvgroup.bpm.core.common.exception.BusinessException;
import com.ngvgroup.bpm.core.common.exception.ErrorCode;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;
import ngvgroup.com.bpm.client.feign.BpmFeignClient;
import ngvgroup.com.loan.core.constant.LoanErrorCode;
import ngvgroup.com.loan.core.constant.LoanVariableConstants;
import ngvgroup.com.loan.feature.product_proccess.dto.*;
import ngvgroup.com.loan.feature.product_proccess.feign.CommonFacFeign;
import ngvgroup.com.loan.feature.product_proccess.mapper.LnmTxnProductMapper;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProduct;
import ngvgroup.com.loan.feature.product_proccess.model.cfg.LnmCfgProductDtl;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProduct;
import ngvgroup.com.loan.feature.product_proccess.model.txn.LnmTxnProductDtl;
import ngvgroup.com.loan.feature.product_proccess.repository.cfg.LnmCfgProductDtlRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.cfg.LnmCfgProductRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.txn.LnmTxnProductDtlRepository;
import ngvgroup.com.loan.feature.product_proccess.repository.txn.LnmTxnProductRepository;
import ngvgroup.com.loan.feature.product_proccess.service.ProductInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductInfoServiceImpl implements ProductInfoService {
    private final LnmCfgProductRepository lnmCfgProductRepository;
    private final LnmCfgProductDtlRepository lnmCfgProductDtlRepository;
    private final LnmTxnProductMapper lnmTxnProductMapper;
    private final CommonFacFeign commonFacFeign;
    private final LnmTxnProductRepository lnmTxnProductRepository;
    private final LnmTxnProductDtlRepository lnmTxnProductDtlRepository;
    private final ExportExcel exportExcel;
    private final BpmFeignClient bpmFeignClient;

    public ProductInfoServiceImpl(LnmCfgProductRepository lnmCfgProductRepository, LnmCfgProductDtlRepository lnmCfgProductDtlRepository, LnmTxnProductMapper lnmTxnProductMapper, CommonFacFeign commonFacFeign, LnmTxnProductRepository lnmTxnProductRepository, LnmTxnProductDtlRepository lnmTxnProductDtlRepository, ExportExcel exportExcel, BpmFeignClient bpmFeignClient) {
        this.lnmCfgProductRepository = lnmCfgProductRepository;
        this.lnmCfgProductDtlRepository = lnmCfgProductDtlRepository;
        this.lnmTxnProductMapper = lnmTxnProductMapper;
        this.commonFacFeign = commonFacFeign;
        this.lnmTxnProductRepository = lnmTxnProductRepository;
        this.lnmTxnProductDtlRepository = lnmTxnProductDtlRepository;
        this.exportExcel = exportExcel;
        this.bpmFeignClient = bpmFeignClient;
    }

    @Override
    public Page<LnmCfgProductDTO> search(String keyword, List<String> commonCodes, Pageable pageable) {
        return lnmCfgProductRepository.search(keyword, commonCodes, pageable);

    }

    @Override
    public ProductDetailDTO getDetailById(Long id) {
        LnmCfgProduct product = lnmCfgProductRepository.findById(id).orElseThrow(() -> new BusinessException(LoanErrorCode.NOT_FOUND));
        ProductDetailDTO productDetailDTO = new ProductDetailDTO();
        ProductProfileDTO productDetails = lnmTxnProductMapper.fromCfgToDto(product);
        List<LnmTxnProductDtlDTO> listDtoDetails = new ArrayList<>();
        List<LnmCfgProductDtl> details = lnmCfgProductDtlRepository.findAllByLnmProductCode(product.getLnmProductCode());
        details.forEach(detail -> {
            LnmTxnProductDtlDTO dtoDetail = lnmTxnProductMapper.toDto(detail);
            listDtoDetails.add(dtoDetail);
        });

        List<ProcessFileDto> files = bpmFeignClient
                .getProcessFilesFromReferenceCode(product.getLnmProductCode(), LoanVariableConstants.PREFIX_PRODUCT_REGISTER).getData();
        productDetailDTO.setProfile(productDetails);
        productDetails.setProductDetails(listDtoDetails);
        productDetailDTO.setFiles(files);
        return productDetailDTO;
    }

    @Override
    public List<String> getAllLnmProductCode() {
        List<String> productCodeList = new ArrayList<>();
        lnmCfgProductRepository.findAll().forEach(product -> productCodeList.add(product.getLnmProductCode()));
        return productCodeList;
    }

    @Override
    public Page<FacCfgAccClassDto> getListFacAccClass(String keyword, Pageable pageable) {
        return Optional.ofNullable(commonFacFeign.searchAll(keyword, pageable).getBody())
                .map(ResponseData::getData)
                .orElse(Page.empty(pageable));
    }

    @Override
    public Page<FacCfgAccStructureDto> getListStructure(Pageable pageable) {
        return Optional.ofNullable(commonFacFeign.getAccStructure(pageable).getBody())
                .map(ResponseData::getData)
                .orElse(Page.empty(pageable));
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        LnmCfgProduct cfgProduct = lnmCfgProductRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND));

        String lnmProductCode = cfgProduct.getLnmProductCode();

        // Soft delete all transaction products
        List<LnmTxnProduct> txnProducts = lnmTxnProductRepository.findAllByLnmProductCode(lnmProductCode);
        txnProducts.forEach(prd -> prd.setIsDelete(1));
        lnmTxnProductRepository.saveAll(txnProducts);

        // Soft delete all transaction product details
        for (LnmTxnProduct txnProduct : txnProducts) {
            List<LnmTxnProductDtl> txnDetails = lnmTxnProductDtlRepository.findByProcessInstanceCode(txnProduct.getProcessInstanceCode());
            txnDetails.forEach(dtl -> dtl.setIsDelete(1));
            lnmTxnProductDtlRepository.saveAll(txnDetails);
        }

        // Soft delete all cfg product details
        List<LnmCfgProductDtl> cfgDetails = lnmCfgProductDtlRepository.findAllByLnmProductCode(lnmProductCode);
        cfgDetails.forEach(dtl -> dtl.setIsDelete(1));
        lnmCfgProductDtlRepository.saveAll(cfgDetails);

        // Soft delete cfg product
        cfgProduct.setIsDelete(1);
        lnmCfgProductRepository.save(cfgProduct);
    }


    @Override
    public ResponseEntity<byte[]> exportExcel(List<LnmCfgProductDTO> list) {
        try {
            return exportExcel.exportExcel(list, "Danh_sach_san_pham");
        } catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR);
        }
    }


    @Override
    public ResponseEntity<byte[]> exportDetail(List<LnmTxnProductDtlDTO> list) {
        try {
            return exportExcel.exportExcel(list, "Danh_sach_chi_tiet_san_pham");
        } catch (Exception e) {
            throw new BusinessException(LoanErrorCode.WRITE_EXCEL_ERROR);
        }
    }
}
