package ngvgroup.com.crm.features.customer.service;

import ngvgroup.com.crm.features.customer.common.GenerateNextSequence;
import ngvgroup.com.crm.features.customer.dto.profile.CustomerProfileDTO;

public interface CustomerTransactionService {

    // --- Transaction Phase (Draft/Process) ---

    /**
     * Tạo mới bản ghi Transaction (Bắt đầu quy trình)
     */
    void createCustomer(CustomerProfileDTO profile);

    /**
     * Cập nhật bản ghi Transaction hiện tại
     */
    void updateCustomer(CustomerProfileDTO profile);

    void updateCustomer(String processInstanceCode, CustomerProfileDTO profile);

    /**
     * Lấy chi tiết hồ sơ từ bảng Transaction theo ProcessInstanceCode
     */
    CustomerProfileDTO getDetail(String processInstanceCode);

    /**
     * Hủy yêu cầu (Cập nhật trạng thái CANCEL)
     */
    void cancelRequest(String processInstanceCode);

    // --- End Phase (Migration) ---

    /**
     * Kết thúc quy trình: Di chuyển dữ liệu từ TXN -> INF -> HISTORY
     */
    void updateEndProcess(String processInstanceCode);

    // --- Utilities ---

    /**
     * Cung cấp bộ sinh mã (để Starter sử dụng)
     */
    GenerateNextSequence getSequence();
}