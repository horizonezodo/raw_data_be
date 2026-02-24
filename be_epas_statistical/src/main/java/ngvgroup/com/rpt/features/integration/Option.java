package ngvgroup.com.rpt.features.integration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DC_OPTION")
public class Option {
    @Id
    private int id;

    @Column(name = "PARAM_CODE", length = 256, nullable = false)
    private String paramCode;

    @Column(name = "PARAM_VALUE", length = 512, nullable = false)
    private String paramValue;
}