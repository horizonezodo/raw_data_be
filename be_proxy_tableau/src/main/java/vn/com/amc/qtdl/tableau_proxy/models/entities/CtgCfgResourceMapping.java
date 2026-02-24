package vn.com.amc.qtdl.tableau_proxy.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "COM_CFG_RESOURCE_MAPPING")
public class CtgCfgResourceMapping extends BaseEntity {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(name = "RESOURCE_TYPE_CODE", length = 64, nullable = false)
    private String resourceTypeCode;
    @Column(name = "USER_ID", length = 64)
    private String userId;
    @Column(name = "GROUP_ID", length = 64)
    private String groupId;
    @Column(name = "RESOURCE_CODE", length = 64, nullable = false)
    private String resourceCode;
    @Column(name = "RESOURCE_NAME", length = 64, nullable = false)
    private String resourceName;
    @Column(name = "RESOURCE_DESC", length = 64)
    private String resourceDesc;
}

