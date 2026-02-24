package vn.com.amc.qtdl.tableau_proxy.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.*;

@Entity
@Table(name = "COM_COMMONS")
@Data
@EqualsAndHashCode(callSuper = true)
public class Common extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "SEQ_COM_COMMONS", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_COM_COMMONS", sequenceName = "SEQ_COM_COMMONS", allocationSize = 1)
    private Long id;

    @Column(name = "COMMON_TYPE_CODE")
    private String commonTypeCode;

    @Column(name = "COMMON_CODE")
    private String commonCode;

    @Column(name = "COMMON_NAME")
    private String commonName;

    @Column(name = "COMMON_VALUE")
    private String commonValue;

    @Column(name = "DESCRIPTION")
    private String description;
}
