package ngvgroup.com.bpm.features.sla.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionMenuDto {
    private String moduleCode;
    private String moduleName;
    private List<CtgCfgProcessTypeDto> processTypes;
}
