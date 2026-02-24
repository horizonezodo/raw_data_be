package ngvgroup.com.ibm.feature.dep_product.service;

import com.ngvgroup.bpm.core.persistence.service.BaseService;
import ngvgroup.com.ibm.feature.dep_product.dto.IbmCfgDepProductDTO;
import ngvgroup.com.ibm.feature.dep_product.model.IbmCfgDepProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IbmCfgDepProductService extends BaseService<IbmCfgDepProduct, IbmCfgDepProductDTO> {
    Page<IbmCfgDepProductDTO> search(String keyword, List<String> ibmDepProductTypeCode, Pageable pageable);
    IbmCfgDepProductDTO getDetail(String ibmDepProductCode);
}