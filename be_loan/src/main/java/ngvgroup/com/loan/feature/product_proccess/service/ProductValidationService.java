package ngvgroup.com.loan.feature.product_proccess.service;

import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;

public interface ProductValidationService {
    void validateProductInfo(ProductProfileDTO profile);
}
