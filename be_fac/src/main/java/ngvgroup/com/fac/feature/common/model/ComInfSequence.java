package ngvgroup.com.fac.feature.common.model;

import com.ngvgroup.bpm.core.persistence.model.BaseEntitySimple;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Immutable;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "COM_INF_SEQUENCE")
@Immutable
public class ComInfSequence extends BaseEntitySimple {

    @Column(name = "ORG_CODE", length = 64)
    private String orgCode;

    @Column(name = "TABLE_NAME", length = 32)
    private String tableName;

    @Column(name = "FIELD_NAME", length = 32)
    private String fieldName;

    @Column(name = "PREFIX", length = 256)
    private String prefix;

    @Column(name = "PERIOD_VALUE", length = 8)
    private String periodValue;

    @Column(name = "CURRENT_SEQ")
    private Long currentSeq;
}
