package ngvgroup.com.bpmn.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_CFG_RESOURCE_MAPPING")
public class CtgCfgResourceMapping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive;

    @Column(name = "RESOURCE_TYPE_CODE", length = 64, nullable = false)
    private String resourceTypeCode;

    @Column(name = "USER_ID", length = 64)
    private String userId;

    @Column(name = "GROUP_ID", length = 64)
    private String groupId;

    @Column(name = "RESOURCE_CODE", length = 128, nullable = false)
    private String resourceCode;

    @Column(name = "RESOURCE_NAME", length = 256, nullable = false)
    private String resourceName;

    @Column(name = "RESOURCE_DESC", length = 256)
    private String resourceDesc;

}
