package ngvgroup.com.loan.feature.common.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CommonDto {
    private String commonCode;
    private String commonName;
    private String parentCode;
    private String commonValue;

    public CommonDto(String commonCode, String commonName) {
        this.commonCode = commonCode;
        this.commonName = commonName;
    }

    public CommonDto(String commonCode, String commonName, String parentCode) {
        this.commonCode = commonCode;
        this.commonName = commonName;
        this.parentCode = parentCode;
    }

    public CommonDto(String commonCode, String commonName, String parentCode, String commonValue) {
        this.commonCode = commonCode;
        this.commonName = commonName;
        this.parentCode = parentCode;
        this.commonValue = commonValue;
    }
}
