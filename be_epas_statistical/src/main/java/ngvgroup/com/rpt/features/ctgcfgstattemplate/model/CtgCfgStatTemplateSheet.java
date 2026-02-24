package ngvgroup.com.rpt.features.ctgcfgstattemplate.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CTG_CFG_STAT_TEMPLATE_SHEET")
public class CtgCfgStatTemplateSheet extends BaseEntity {
    @Column(name = "ORG_CODE", length = 64, nullable = false)
    private String orgCode;

    @Column(name = "TEMPLATE_CODE", length = 128, nullable = false)
    private String templateCode;

    @Column(name = "SHEET_DATA", length = 64)
    private String sheetData;

    @Column(name = "AREA_ID", length = 2)
    private double areaId;

    @Column(name = "COLUMN_START", length = 5)
    private double columnStart;

    @Column(name = "ROW_START", length = 5)
    private double rowStart;

    @Column(name = "COLUMN_END", length = 5)
    private double columnEnd;

    @Column(name = "ROW_END", length = 5)
    private double rowEnd;

    @Column(name = "ROWS_TO_DELETE", length = 5)
    private double rowToDelete;

    @Column(name = "TABLE_DATA", length = 64)
    private String tableData;

    @Column(name = "RECORD_STATUS", length = 64)
    private String recordStatus = "approval";

    @Column(name = "IS_ACTIVE", nullable = false)
    private Integer isActive = 1;

}
