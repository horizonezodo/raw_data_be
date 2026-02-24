package ngvgroup.com.bpm.features.common.dto;

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
}
