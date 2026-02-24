package ngvgroup.com.loan.feature.product_proccess.service;

import ngvgroup.com.loan.core.utils.GenerateNextSequence;
import ngvgroup.com.loan.feature.product_proccess.dto.ProductProfileDTO;

public interface ProductTransactionService {

    void createProduct(ProductProfileDTO profile);

    void updateProduct(ProductProfileDTO profile);

    void updateProduct(String processInstanceCode, ProductProfileDTO profile);

    ProductProfileDTO getDetail(String processInstanceCode);

    void cancelRequest(String processInstanceCode);

    void updateEndProcess(String processInstanceCode, String type);

    GenerateNextSequence getSequence();
}
