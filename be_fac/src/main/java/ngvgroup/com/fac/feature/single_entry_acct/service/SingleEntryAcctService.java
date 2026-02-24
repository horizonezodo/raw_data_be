package ngvgroup.com.fac.feature.single_entry_acct.service;

import ngvgroup.com.fac.feature.single_entry_acct.dto.SingleEntryAcctDTO;

public interface SingleEntryAcctService {
    // --- Transaction Phase (Draft/Process) ---

    /**
     * Tạo mới bản ghi Transaction (Bắt đầu quy trình)
     */
    void createAccountEntry(SingleEntryAcctDTO dto, String processInstanceCode);

    /**
     * Cập nhật bản ghi Transaction hiện tại
     */
    void updateAccountEntry(SingleEntryAcctDTO profile, String processInstanceCode);

    /**
     * Lấy chi tiết hồ sơ từ bảng Transaction theo ProcessInstanceCode
     */
    SingleEntryAcctDTO getDetail(String processInstanceCode);

    /**
     * Hủy yêu cầu (Cập nhật trạng thái CANCEL)
     */
    void cancelRequest(String processInstanceCode);

    // --- End Phase (Migration) ---

    /**
     * Kết thúc quy trình: Di chuyển dữ liệu từ TXN -> INF -> HISTORY
     */
    void updateEndProcess(String processInstanceCode);
}
