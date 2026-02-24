package ngvgroup.com.loan.feature.product_proccess.service;

import ngvgroup.com.loan.feature.product_proccess.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductInfoService {
    Page<LnmCfgProductDTO> search(String keyword, List<String> commonCodes, Pageable pageable);

    ProductDetailDTO getDetailById(Long id);

    List<String> getAllLnmProductCode();

    Page<FacCfgAccClassDto> getListFacAccClass(String keyword, Pageable pageable);

    Page<FacCfgAccStructureDto> getListStructure(Pageable pageable);

    void deleteProduct(Long id);

    ResponseEntity<byte[]> exportExcel(List<LnmCfgProductDTO> list);

    ResponseEntity<byte[]> exportDetail(List<LnmTxnProductDtlDTO> listDetail);
}
