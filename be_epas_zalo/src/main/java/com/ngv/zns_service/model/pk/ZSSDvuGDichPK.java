package com.ngv.zns_service.model.pk;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSDvuGDichPK implements Serializable {
    private String maDvu;
    private String maLoaiGdich;
}

