package ngvgroup.com.crm.features.customer.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerSearchResultDTO {
    @ExcelColumn(value = "TT")
    private Long id;
    @ExcelColumn(value = "Mã khách hàng")
    private String customerCode;
    @ExcelColumn(value = "Tên khách hàng")
    private String customerName;
    private String customerType;
    @ExcelColumn(value = "Loại KH")
    private String customerTypeName;
    @ExcelColumn(value = "Số di động")
    private String mobileNumber;
    @ExcelColumn(value = "CCCD | ĐKKD")
    private String identificationId;
    @ExcelColumn(value = "Địa chỉ")
    private String address;
}
