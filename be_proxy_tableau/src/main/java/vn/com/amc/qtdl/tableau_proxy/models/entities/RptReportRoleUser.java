package vn.com.amc.qtdl.tableau_proxy.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "RPT_REPORT_ROLE_USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RptReportRoleUser extends BaseEntity {
    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "SEQ_RPT_REPORT_ROLE_USER", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "SEQ_RPT_REPORT_ROLE_USER", sequenceName = "SEQ_RPT_REPORT_ROLE_USER", allocationSize = 1)
    private Long id;

    @Column(name = "REPORT_ROLE_ID", nullable = false, length = 64)
    private String reportRoleId;

    @Column(name = "USER_ID", nullable = false, length = 256)
    private String userId;
}