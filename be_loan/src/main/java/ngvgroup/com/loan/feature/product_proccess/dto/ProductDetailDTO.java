package ngvgroup.com.loan.feature.product_proccess.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ngvgroup.com.bpm.client.dto.shared.ProcessFileDto;

import java.util.List;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class ProductDetailDTO {
    private ProductProfileDTO profile;
    private List<ProcessFileDto> files;
}
