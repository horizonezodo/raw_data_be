package ngvgroup.com.ibm.feature.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class ComCfgBaseIntDto implements Serializable {
    private final String baseIntCode;
    private final String baseIntName;
    private final Integer intNumr;
    private final Integer intDnmr;
}