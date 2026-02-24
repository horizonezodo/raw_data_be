package com.ngv.zns_service.dto.response.goiDv;

import com.ngv.zns_service.dto.response.dvu.ZSSDvuDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ZSSGoiDvuDto {
    private String oaId;
    private String maGoiDvu;
    private String nguoiNhap;
    private String ngayNhap;
    private String nguoiSua;
    private String ngaySua;
    private String tenGoiDvu;
    private String tthaiNvu;
    private String mota;
    private List<ZSSDvuDto> zssDvuDtoList;
}
