package com.ngv.zns_service.dto.response.gDich;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZSSGDichDto {
    private String maLoaiGdich;
    private String maDvu;
    private String nguoiNhap;
    private String ngayNhap;
    private String nguoiSua;
    private String ngaySua;
}