package vn.com.amc.qtdl.tableau_proxy.models.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "REPORT_MAPPING")
@Accessors(chain = true)
public class ReportMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_mapping_seq")
    @SequenceGenerator(name = "report_mapping_seq", sequenceName = "report_mapping_seq", allocationSize = 1)
    private Long id;
    private String username;
    private String reportName;
}
