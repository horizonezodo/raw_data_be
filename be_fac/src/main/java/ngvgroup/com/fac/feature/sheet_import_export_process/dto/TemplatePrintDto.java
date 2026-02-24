package ngvgroup.com.fac.feature.sheet_import_export_process.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TemplatePrintDto {
    // Header - Thông tin tổ chức & Chứng từ
    private String orgName;        // CT01 (Tên đơn vị)
    private String orgAddress;     // CT01 (Địa chỉ đơn vị)
    private String voucherNo;      // Số chứng từ (VD: 01NB001)
    private String voucherTypeName;// CT02 (Tên loại chứng từ: PHIẾU NHẬP NGOẠI BẢNG)
    private LocalDate txnDate;          // CT03 (Ngày tháng năm)

    // Customer/Partner Info
    private String customerName;
    private String address;// (5) Địa chỉ
    private String phoneNumber;    // (6) Điện thoại (Logic phức tạp)
    private String identificationId;   // (7) Số CCCD
    private LocalDate issueDate;      // (8) Ngày cấp
    private String issuePlace;     // (9) Nơi cấp

    // Table Details (Danh sách tài khoản)
    private List<TemplateDetailDto> details;

    // Footer / Totals
    private BigDecimal totalAmount;    // (14) Tổng tiền bằng số
    private String totalAmountWord;    // (15) Tổng tiền bằng chữ
    private String attachments;        // (16) Chứng từ kèm theo
    private String txnContent;        // (17) Diễn giải

    // Signatures (Có thể truyền tên người ký nếu cần)
    private String accountant;         // (18) Kế toán
    private String chiefAccountant;    // (19) Kế toán trưởng
    private String director;           // (20) Giám đốc

    private String voucherTypeCode;
}
