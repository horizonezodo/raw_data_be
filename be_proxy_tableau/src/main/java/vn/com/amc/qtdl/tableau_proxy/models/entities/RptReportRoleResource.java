package vn.com.amc.qtdl.tableau_proxy.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "RPT_REPORT_ROLE_RESOURCE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RptReportRoleResource extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "SEQ_RPT_REPORT_ROLE_RESOURCE", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_RPT_REPORT_ROLE_RESOURCE", sequenceName = "SEQ_RPT_REPORT_ROLE_RESOURCE", allocationSize = 1)
    private Long id;

    @Column(name = "RESOURCE_MASTER", nullable = false, length = 64)
    private String resourceMaster;

    @Column(name = "RESOURCE_TYPE_CODE", nullable = true, length = 64)
    private String resourceTypeCode;

    @Column(name = "REPORT_ROLE_ID", nullable = false, length = 64)
    private String reportRoleId;

    @Column(name = "RESOURCE_CODE", nullable = false, length = 64)
    private String resourceCode;

    @Column(name = "RESOURCE_NAME", nullable = true, length = 256)
    private String resourceName;

    @Column(name = "RESOURCE_DESC", nullable = true, length = 256)
    private String resourceDesc;
}