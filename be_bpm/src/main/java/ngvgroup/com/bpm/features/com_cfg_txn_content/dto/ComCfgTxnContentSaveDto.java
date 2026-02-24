package ngvgroup.com.bpm.features.com_cfg_txn_content.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgTxnContentSaveDto {
    private Long id;

    @NotBlank
    private String contentCode;

    @NotBlank
    private String contentName;

    @NotBlank
    private String orgCode;

    @NotBlank
    private String moduleCode;

    @NotNull
    private Integer length;

    private String contentText;

    private List<ComCfgTxnContentSaveDtlDto> details;
}
