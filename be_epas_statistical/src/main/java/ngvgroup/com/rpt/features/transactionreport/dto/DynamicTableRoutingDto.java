package ngvgroup.com.rpt.features.transactionreport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicTableRoutingDto {
    private String templateCode;    // Để phân biệt nếu sửa nhiều template cùng lúc
    private String targetKpiCode;   // TemplateKpiCode
    private String targetTableName; // Tên bảng đích (tableData)
}