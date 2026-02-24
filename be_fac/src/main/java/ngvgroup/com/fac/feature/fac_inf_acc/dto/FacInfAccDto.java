package ngvgroup.com.fac.feature.fac_inf_acc.dto;

import com.ngvgroup.bpm.core.common.excel.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ngvgroup.com.fac.core.constant.ExcelColumns;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
public class FacInfAccDto {
    private Long id;

    @ExcelColumn(ExcelColumns.HEADER_ACC_NO)
    private String accNo;

    @ExcelColumn(ExcelColumns.HEADER_ACC_NAME)
    private String accName;

    @ExcelColumn(ExcelColumns.HEADER_BAL)
    private BigDecimal bal;

    @ExcelColumn(ExcelColumns.HEADER_ACC_NATURE)
    private String accNature;

    @ExcelColumn(ExcelColumns.HEADER_ACC_CLASS_CODE)
    private String accClassCode;

    @ExcelColumn(ExcelColumns.HEADER_ACC_STATUS)
    private String accStatus;

    private BigDecimal balAvailable;

    private BigDecimal balActual;

    private Timestamp modifiedDate;

    public FacInfAccDto(String accNo, String accName, BigDecimal bal, String accNature, String accStatus) {
        this.accNo = accNo;
        this.accName = accName;
        this.bal = bal;
        this.accNature = accNature;
        this.accStatus = accStatus;
    }

    public FacInfAccDto(Long id, String accNo, String accName, BigDecimal bal, String accNature, String accStatus) {
        this.id = id;
        this.accNo = accNo;
        this.accName = accName;
        this.bal = bal;
        this.accNature = accNature;
        this.accStatus = accStatus;
    }
}
