package ngvgroup.com.rpt.features.smrtxnscore.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NextStepDto {
    private String name;
    private String code;
    private String transitionCode;
    private String transitionName; // tên hành động trước đó

    public NextStepDto(String name, String code, String transitionCode, String transitionName) {
        this.name = name;
        this.code = code;
        this.transitionCode = transitionCode;
        this.transitionName = transitionName;
    }
}
