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
@Table(name = "CTG_CFG_PROCESS_GROUP")
public class CtgCfgProcessGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "RECORD_STATUS", length = 64, nullable = false)
    private String recordStatus;

    @Column(name = "PROCESS_GROUP_CODE", length = 64, nullable = false)
    private String processGroupCode;

    @Column(name = "PROCESS_GROUP_NAME", length = 128)
    private String processGroupName;
}
