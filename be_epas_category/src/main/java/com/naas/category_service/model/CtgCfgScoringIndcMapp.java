package com.naas.category_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CTG_CFG_SCORING_INDC_MAPP")
public class CtgCfgScoringIndcMapp extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "IS_ACTIVE", nullable = false)
    private int isActive;
    @Column(name = "RECORD_STATUS",length = 64,nullable = false)
    private String recordStatus;
    @Column(name = "SORT_NUMBER", length = 22, nullable = false)
    private int sortNumber;
    @Column(name = "SCORING_INDC_GROUP_CODE", length = 128, nullable = false)
    private String scoringIndcGroupCode;
    @Column(name = "INDICATOR_CODE", length = 128, nullable = false)
    private String indicatorCode;
}
