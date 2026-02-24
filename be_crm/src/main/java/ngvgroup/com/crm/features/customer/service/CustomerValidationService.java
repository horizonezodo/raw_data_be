package ngvgroup.com.crm.features.customer.service;

import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;

public interface CustomerValidationService {
    /**
     * Kiểm tra toàn bộ thông tin khách hàng.
     * Ném ra BusinessException nếu có dữ liệu không hợp lệ.
     *
     * @param profile Dữ liệu khách hàng từ FE hoặc BPM
     */
    void validateCustomerInfo(CustomerProfileDTO profile);

    void validateCustomerCode(CustomerProfileDTO profile);
}