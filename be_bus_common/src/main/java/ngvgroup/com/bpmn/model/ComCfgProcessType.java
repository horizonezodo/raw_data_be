package ngvgroup.com.bpmn.model;

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
@Table(name = "CTG_CFG_PROCESS_TYPE")
public class ComCfgProcessType extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "IS_ACTIVE", nullable = false)
    private String isActive;

    @Column(name = "PROCESS_TYPE_CODE", length = 128, nullable = false)
    private String processTypeCode;

    @Column(name = "PROCESS_TYPE_NAME", length = 256)
    private String processTypeName;

    @Column(name = "PROCESS_TYPE", length = 128, nullable = true)
    private String processType;

    @Column(name = "PROCESS_GROUP_CODE", length = 64, nullable = false)
    private String processGroupCode;

    @Column(name = "PROCESS_GROUP_NAME", length = 128, nullable = false)
    private String processGroupName;

}



