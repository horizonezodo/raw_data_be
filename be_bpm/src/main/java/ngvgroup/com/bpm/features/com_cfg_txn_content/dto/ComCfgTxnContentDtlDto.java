package ngvgroup.com.bpm.features.com_cfg_txn_content.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComCfgTxnContentDtlDto {

    private Long id;

    private String orgCode;

    private String contentDtlCode;

    private String contentValueTypeName;

    private String contentValue;

    private Integer sortNumber;

    private String formatMask;

    private Integer length;

}
