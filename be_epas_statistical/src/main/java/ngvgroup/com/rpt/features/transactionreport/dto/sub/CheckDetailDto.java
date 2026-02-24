package ngvgroup.com.rpt.features.transactionreport.dto.sub;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckDetailDto {
    private Long id;
    private String statInstantCode;
    private String code;
    private String name;
    private String templateCode;
}
