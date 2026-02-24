package ngvgroup.com.ibm.feature.dep_product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IbmCfgDepProductDTO {
    private Long id;
    private String orgCode;
    private String currencyCode;
    private String ibmDepProductCode;
    private String ibmDepProductName;
    private String ibmDepProductTypeCode;
    private String interestPaymentMethod;
    private String accClassCode;
    private String accStructureCode;
    private Integer termValue;
    private String termUnit;
    private String interestRateCode;
    private String earlyInterestCode;
    private java.sql.Timestamp effectiveDate;
    private java.sql.Timestamp expiryDate;
    private String description;


    private String ibmDepProductTypeName;
    private String interestPaymentMethodName;
    private String interestRateTypeName;
    private List<IbmCfgDepProductDtlDTO> cfgDepProductDtlDTOs;

    public IbmCfgDepProductDTO(Long id, String ibmDepProductCode, String ibmDepProductName, String ibmDepProductTypeName, String interestPaymentMethodName, String interestRateTypeName) {
        this.id = id;
        this.ibmDepProductCode = ibmDepProductCode;
        this.ibmDepProductName = ibmDepProductName;
        this.ibmDepProductTypeName = ibmDepProductTypeName;
        this.interestPaymentMethodName = interestPaymentMethodName;
        this.interestRateTypeName = interestRateTypeName;
    }
}