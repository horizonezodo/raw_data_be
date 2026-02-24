package com.naas.admin_service.features.category.model;

import java.math.BigDecimal;

import com.ngvgroup.bpm.core.persistence.model.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COM_CFG_TEMPLATE")
public class ComCfgTemplate extends BaseEntity {
  @Column(name = "TEMPLATE_CODE", nullable = false, length = 128)
  private String templateCode;

  @Column(name = "FILE_NAME", nullable = false, length = 256)
  private String fileName;

  @Column(name = "FILE_PATH", nullable = false, length = 256)
  private String filePath;

  @Column(name = "FILE_SIZE", precision = 10, scale = 3)
  private BigDecimal fileSize;

  @Column(name = "REQUIRED_ACTION", nullable = false, length = 256)
  private String requiredAction;

  @Column(name = "RECORD_STATUS", length = 64)
  private String recordStatus = "approval";

  @Column(name = "IS_ACTIVE", nullable = false)
  private Integer isActive = 1;

  @Column(name = "MAPPING_FILE_PATH", length = 256)
  private String mappingFilePath;
}
