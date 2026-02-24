package ngvgroup.com.loan.feature.product_proccess.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductProfileDTO {

    @NotBlank
    private String orgCode;

    @NotBlank
    private String currencyCode;

    private String processInstanceCode;

    @NotBlank
    private String lnmProductCode;

    @NotBlank
    private String lnmProductName;

    @NotBlank
    private String lnmProductTypeCode;

    @NotBlank
    private String loanTermTypeCode;

    @NotBlank
    private String interestRateCode;

    @NotBlank
    private String accClassCode;

    @NotBlank
    private String accStructureCode;

    @NotBlank
    private Integer loanTermFrom;

    @NotBlank
    private Integer loanTermTo;

    private Date effectiveDate;

    private Date expiryDate;

    private String description;

    private List<LnmTxnProductDtlDTO> productDetails;
}
