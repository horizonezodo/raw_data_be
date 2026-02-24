package com.naas.category_service.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "CTG_COM_COMMON")
public class CtgComCommon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private String isActive;

    @Column(name = "COMMON_TYPE_CODE", length = 32, nullable = false)
    private String commonTypeCode;

    @Column(name = "COMMON_TYPE_NAME", length = 128, nullable = false)
    private String commonTypeName;

    @Column(name = "COMMON_CODE", length = 32, nullable = false)
    private String commonCode;

    @Column(name = "COMMON_NAME", length = 256, nullable = false)
    private String commonName;

    @Column(name = "COMMON_VALUE", length = 64)
    private String commonValue;

    @Column(name = "PARENT_CODE", length = 128)
    private String parentCode;

    @Column(name = "SORT_NUMBER")
    private Integer sortNumber;

    @Column(name = "CONTROL_TYPE", length = 64)
    private String controlType;
}

