package com.ngv.aia_service.intergation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "DC_OPTION")
public class Option {
    @Id
    private int id;

    @Column(name = "PARTNER_CODE", length = 32, nullable = false)
    private String partnerCode;

    @Column(name = "PARAM_CODE", length = 256, nullable = false)
    private String paramCode;

    @Column(name = "PARAM_VALUE", length = 512, nullable = false)
    private String paramValue;
}
